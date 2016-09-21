/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class UtilsGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        /** Run a console command and return the output */
        project.ext.cmd = { String[] input ->
            def out = new ByteArrayOutputStream()
            try {
                exec {
                    commandLine input
                    standardOutput = out
                }

                return out.toString().trim()
            } catch (e) { // cmd not found on system
                return "unknown"
            }
        }

        /** Grab the git hash id, will throw a problem if not a git repo */
        project.ext.git = {
            cmd('git', 'log', '--pretty=format:%h', '-1')
        }
    }
}
