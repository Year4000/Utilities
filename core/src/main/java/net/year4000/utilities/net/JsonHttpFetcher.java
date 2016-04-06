/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AbstractHttpFetcher.ContentType("application/json; charset=utf8")
public class JsonHttpFetcher extends AbstractHttpFetcher<JsonObject> {
    private final Gson GSON;

    private JsonHttpFetcher(int maxTries, Gson gson, ExecutorService executorService) {
        super(maxTries, executorService);
        GSON = gson;
    }

    /** Get the builder that will create this HttpFetcher */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected <T> T reader(Reader reader, Type type) {
        return GSON.fromJson(reader, type);
    }

    @Override
    protected <T> T reader(Reader reader, Class<T> type) {
        return reader(reader, (Type) type);
    }

    @Override
    protected String serialize(JsonObject object) {
        return GSON.toJson(object);
    }

    /** {@inheritDoc} */
    public static class Builder extends AbstractHttpFetcher.AbstractBuilder<JsonHttpFetcher, Builder> {
        private Optional<Gson> gson = Optional.empty();

        /** The Gson object used for handling JSON requests */
        public Builder gson(Gson gson) {
            this.gson = Optional.ofNullable(gson);
            return this;
        }

        @Override
        public JsonHttpFetcher build() {
            return new JsonHttpFetcher(
                maxTries.orElse(3),
                gson.orElse(new GsonBuilder().disableHtmlEscaping().create()),
                executorService.orElse(Executors.newCachedThreadPool())
            );
        }
    }
}
