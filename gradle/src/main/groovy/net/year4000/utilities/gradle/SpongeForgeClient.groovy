/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.gradle

import com.google.inject.Inject
import groovy.transform.ToString
import net.lingala.zip4j.core.ZipFile
import net.year4000.utilities.OS
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

import java.util.stream.Collectors

import static net.year4000.utilities.gradle.SpongeGradlePlugin.MINECRAFT_HOME
import static net.year4000.utilities.gradle.SpongeGradlePlugin.PS
import static net.year4000.utilities.gradle.SpongeGradlePlugin.GSON

/** Sponge starter runs the needed things to start a sponge instance */
class SpongeForgeClient extends DefaultTask {
    SpongeForgeClient() { dependsOn += 'assemble' }

    @Inject
    private Project project

    @TaskAction
    def startForgeClient() {
        // Ensure user has a copy of Minecraft
        if (!MINECRAFT_HOME.exists()) {
            throw new Throwable('''
                Minecraft path does not exists, you MUST have a copy of Minecraft, you can grab it from here;
                https://minecraft.net/
            ''')
        }

        File versions = new File(MINECRAFT_HOME, 'versions')
        Optional<File> forge = Arrays.asList(versions.listFiles()).stream()
                .filter({file -> file.name.contains('forge')})
                .sorted().sorted(Comparator.reverseOrder())
                .findFirst()

        // Ensure user has a copy of MinecraftForge
        if (!forge.isPresent()) {
            throw new Throwable('''
                You do not have forge please install forge, you can grab it from here;
                http://files.minecraftforge.net/
            ''')
        }

        logger.lifecycle 'Copying project jar to mods folder'
        File buildJar
        try {
            buildJar = project.shadowJar.archivePath
        } catch (any) {
            logger.debug(any.message)
            buildJar = project.jar.archivePath
        }
        File forgeClient = new File(project.rootDir, '/run/forge-client/')
        System.setProperty("spongeplugin.mcpath", forgeClient.toString())
        File pluginJarPath = new File(forgeClient, '/mods/')
        File pluginJar = new File(pluginJarPath, buildJar.name)
        pluginJarPath.mkdirs()
        Files.copy(buildJar, pluginJar)

        if (project.spongestarter.deleteJar) {
            pluginJar.deleteOnExit()
        }

        logger.lifecycle 'Setting up the client enverionment'
        MinecraftVersion version = makeMCVersion forge.get()
        ClassPaths classPaths = generateLibs version;
        classPaths.libs.get().add("${MINECRAFT_HOME}/versions${PS}${version.inheritsFrom}${PS}${version.inheritsFrom}.jar")
        def classpath = classPaths.libs.get().stream().collect(Collectors.joining(File.pathSeparator))
        extractJars classPaths.natives.get(), new File(MINECRAFT_HOME, 'natives')
        String[] args = minecraftArguments(version, classpath, loginDetails())

        logger.lifecycle 'Starting the Minecraft Client, using settings from the Minecraft Launcher'
        Process process = new ProcessBuilder(args).directory(forgeClient).start()
        Runtime.runtime.addShutdownHook { process.destroy() }
        new StreamToLogger(src: process.inputStream, logger: logger, level: LogLevel.WARN).execute()
        new StreamToLogger(src: process.errorStream, logger: logger, level: LogLevel.ERROR).execute()
        process.waitFor()
    }

    /** Get the login details of the client */
    private String[] loginDetails() {
        File profiles = new File(MINECRAFT_HOME, 'launcher_profiles.json')
        def details = []
        boolean profileRand = new Random().nextBoolean()
        def info = "(name:${->details[0]}, token:${->details[1]}, uuid:${->details[2]})"
        def defaultDetails = [
                profileRand ? 'Alex' : 'Steve',
                UUID.randomUUID().toString().replace('-', ''),
                profileRand ? 'fffffff0fffffff0fffffff0fffffff1' : 'fffffff0fffffff0fffffff0fffffff0'
        ]

        // Use Alex or Steve if launcher_profiles.json does not exist
        if (!profiles.exists()) {
            details = defaultDetails
            logger.lifecycle "Profile could not be found using: $info"
            return details
        }

        FileReader reader = new FileReader(profiles)
        LauncherProfiles profile = GSON.fromJson(reader, LauncherProfiles)
        reader.close()
        details = [profile.displayName(), profile.accessToken(), profile.uuid()]

        if (profile.displayName() == null) {
            details = defaultDetails
        }

        logger.lifecycle "Profile found using: $info"

        return details
    }

    /** Create a string array of the arguments including var filtering */
    private List<String> minecraftArguments(MinecraftVersion version, String classpath, String... login) {
        Objects.requireNonNull(version)
        String[] argParts = version.minecraftArguments.split(' ')
        def minecraftArgs = [
                'java',
                "-Djava.library.path=${MINECRAFT_HOME}/natives/",
                "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${project.spongestarter.debugPort}",
                // todo launcher java args
                '-cp',
                classpath,
                version.mainClass
        ]
        def varMap = [
                assets_index_name: version.assets,
                version_name: version.id,
                game_directory: System.getProperty('spongeplugin.mcpath'),
                assets_root: "${MINECRAFT_HOME}/assets/",
                version_type: version.type,
                user_type: 'mojang',
                user_properties: '{}',
                auth_player_name: login[0],
                auth_access_token: login[1],
                auth_uuid: login[2]
        ]

        argParts.each {
            if (it.startsWith('${') && it.endsWith('}')) {
                def var = it.substring(2, it.size() - 1)
                if (varMap[var] == null || varMap[var] == '') {
                    minecraftArgs.remove(minecraftArgs.size() - 1)
                } else {
                    minecraftArgs.add varMap[var]
                }
            } else {
                minecraftArgs.add it
            }
        }

        return minecraftArgs
    }

    /** Extract the jars to the selected path */
    private def extractJars(List<String> paths, File extractTo) {
        Objects.requireNonNull(paths)
        Objects.requireNonNull(extractTo)

        if (extractTo.exists()) {
            extractTo.delete()
        }

        extractTo.mkdirs()
        extractTo.deleteOnExit()
        def erred = false

        paths.each {
            def file = new File(it)
            if (file.exists()) {
                new ZipFile(file).extractAll extractTo.getPath()
            } else {
                logger.debug("no file found by the name of: ${file.name}")
                erred = true
            }
        }

        if (erred) {
            logger.warn('There were native jars not found, minecraft may fail to load')
        }
    }

    /** Convert the json file to MinecraftVersion object */
    private MinecraftVersion makeMCVersion(File file) {
        Objects.requireNonNull(file)
        file = new File(file, file.name + '.json')
        FileInputStream stream = new FileInputStream(file)
        InputStreamReader reader = new InputStreamReader(stream)
        MinecraftVersion version = GSON.fromJson reader, MinecraftVersion
        logger.debug("Reading ${file.name}: ${version}")
        reader.close();
        return version;
    }

    /** Generate a list of ClassPaths minecraft needs */
    private ClassPaths generateLibs(MinecraftVersion object) {
        Objects.requireNonNull(object)
        ClassPaths classPaths = new ClassPaths();

        if (object.inheritsFrom != null) {
            File path = new File(new File(MINECRAFT_HOME, 'versions'), object.inheritsFrom)
            MinecraftVersion version = makeMCVersion path
            ClassPaths parent = generateLibs version
            classPaths.libs.get().addAll parent.libs.get()
            classPaths.natives.get().addAll parent.natives.get()
        }

        Collections.reverse object.libraries

        object.libraries.each {
            String[] parts = ((String) it.get('name')).split(':')
            String library = "${MINECRAFT_HOME}/libraries$PS${parts[0].replace('.', PS)}$PS${parts[1]}$PS${parts[2]}$PS${parts[1]}-${parts[2]}.jar"

            if (it.containsKey('natives')) {
                def platform = OS.detect().name().toLowerCase()
                def nativeName = library.substring(0, library.lastIndexOf('.jar')) + '-natives-' + platform + '.jar'
                classPaths.natives.get().addFirst nativeName
            } else {
                classPaths.libs.get().addFirst library
            }
        }

        return classPaths;
    }

    @ToString(includeNames=true)
    private class LauncherProfiles {
        private String selectedUser
        private Map<String, Object> authenticationDatabase

        def accessToken() { authenticationDatabase.get(selectedUser).accessToken }
        def displayName() { authenticationDatabase.get(selectedUser).displayName }
        def uuid() { selectedUser }
    }

    @ToString(includeNames=true)
    private class MinecraftVersion {
        String id
        String type
        String inheritsFrom
        String assets
        String minecraftArguments
        String mainClass
        List<Map<String, Object>> libraries
    }

    private class ClassPaths extends Tuples.Pair<LinkedList<String>, LinkedList<String>> {
        def libs = a
        def natives = b

        ClassPaths() { super(new LinkedList<String>(), new LinkedList<String>()) }
    }
}
