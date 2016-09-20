/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.gradle

import com.google.gson.Gson
import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Stage
import net.year4000.utilities.OS
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpongeGradlePlugin implements Plugin<Project> {
    static final Gson GSON = new Gson()
    static final OS PLATFORM = OS.detect()
    static final String PS = File.separator
    static final File MINECRAFT_HOME = {
        def home = System.getProperty('user.home')
        switch (PLATFORM) {
            case OS.LINUX:
                return new File(home, '.minecraft')
            case OS.OSX:
                return new File(home, '/Library/Application Support/minecraft')
                break
            case OS.WINDOWS:
                return new File(home, 'Application Data\\.minecraft')
        }
    }.call()

    @Override
    void apply(Project project) {
        Injector injector = Guice .createInjector Stage.DEVELOPMENT, new Module() {
            @Override
            void configure(Binder binder) {
                binder.bind(Project).toInstance(project)
                binder.bind(SpongeGradlePlugin).toInstance(SpongeGradlePlugin.this)
            }
        }

        project.extensions.create('spongestarter', SpongeExtension)

        /** Start the forge client */
        injector.injectMembers(project.tasks.create('startForgeClient', SpongeForgeClient))
    }
}