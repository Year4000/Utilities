package net.year4000.utilities.api;

import lombok.Data;

import java.net.URL;
import java.util.Map;

@Data
public class API {
    String name;
    String vendor;
    String version;
    URL documentation;
    Map<String, String> routes;
}
