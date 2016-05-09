package net.year4000.utilities.ducktape;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import scala.collection.JavaConversions;
import scala.collection.immutable.List;
import scala.reflect.internal.util.ScalaClassLoader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Set;

public class ScalaModuleLoader implements ModuleLoader {

    /** Load any scala script that ends with .scala */
    @Override
    public Collection<Class<?>> load(Path dir) throws IOException {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        Class<?> clazzLoader = ModuleManager.class;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(checkNotNull(dir))) {
            for (Path path : stream) {
                if (!path.toString().endsWith(".scala")) {
                    continue;
                }

                URL url = path.toUri().toURL();
                URL[] urls = new URL[] {url};

                ClassLoader parent = AccessController.doPrivileged((PrivilegedAction<URLClassLoader>) () ->
                        new URLClassLoader(urls, clazzLoader.getClassLoader())
                );

                // Convert the Groovy script to a Java Class
                List<URL> urlList = JavaConversions.asScalaBuffer(Lists.newArrayList(urls)).toList();
                ScalaClassLoader loader = new ScalaClassLoader.URLClassLoader(urlList, parent);
                System.out.println(path.toAbsolutePath().toString());
                Object obj = loader.tryToLoadClass("scala_plugin").get();
                System.out.println(obj);
                System.out.println(obj.getClass());
                classes.add(obj.getClass());
            }
        }

        return classes;
    }
}
