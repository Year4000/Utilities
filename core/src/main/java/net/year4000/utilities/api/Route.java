package net.year4000.utilities.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Route<T> {
    @Getter
    private final String routeUrl;
    private final Class<T> clazz;

    public T getData() {
        return APIManager.readJson(getRouteUrl() + getExtraData(), clazz);
    }

    public String getExtraData() {
        return "";
    }
}
