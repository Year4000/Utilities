/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class URLBuilderTest {

    @Test
    public void urls() throws IOException {
        String host = URLBuilder.builder("localhost").build();
        String paths = URLBuilder.builder("localhost")
            .addPath("hello/world")
            .build();
        String path = URLBuilder.builder("localhost")
            .addPath(123)
            .addPath("smith")
            .addPath()
            .build();
        String query = URLBuilder.builder("localhost")
            .addQuery("hello")
            .addQuery("key", "123456789")
            .build();
        String queryPath = URLBuilder.builder("localhost")
            .addPath("hello")
            .addQuery("key", 123456789)
            .build();
        String fromURL = URLBuilder.fromURL("http://localhost/jhon/smith?fname=bobby&lname=bob")
            .build();


        Assert.assertEquals(host, "http://localhost/");
        Assert.assertEquals(paths, "http://localhost/hello/world");
        Assert.assertEquals(path, "http://localhost/123/smith/");
        Assert.assertEquals(query, "http://localhost/?hello=&key=123456789");
        Assert.assertEquals(queryPath, "http://localhost/hello?key=123456789");
        Assert.assertEquals(fromURL, "http://localhost/jhon/smith?fname=bobby&lname=bob");
    }
}
