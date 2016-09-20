/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.gradle

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/** Run Streams to Logger */
final class StreamToLogger implements Runnable {
    static final ExecutorService EXECUTOR = Executors.newCachedThreadPool()
    InputStream src
    Logger logger
    LogLevel level

    private InputStreamReader reader
    private BufferedReader bufferedReader

    @Override
    void run() {
        if (reader == null && bufferedReader == null) {
            reader = new InputStreamReader(src)
            bufferedReader = new BufferedReader(reader)
        }

        String line = bufferedReader.readLine()
        if (line != null) {
            logger.log level, line
        }
        EXECUTOR.execute this
    }

    def execute() {
        EXECUTOR.execute this
        Runtime.runtime.addShutdownHook { EXECUTOR.shutdown }
    }
}

