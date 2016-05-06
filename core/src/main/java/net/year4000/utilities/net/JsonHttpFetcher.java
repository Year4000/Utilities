/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.value.Value;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AbstractHttpFetcher.ContentType("application/json; charset=utf8")
public class JsonHttpFetcher extends AbstractHttpFetcher<JsonObject> {
    private final Gson GSON;

    private JsonHttpFetcher(int maxTries, Gson gson, ExecutorService executorService) {
        super(maxTries, executorService);
        GSON = Conditions.nonNull(gson, "gson");
    }

    /** Get the builder that will create this HttpFetcher */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected <T> T reader(Reader reader, Type type) {
        Conditions.nonNull(reader, "reader");
        Conditions.nonNull(type, "type");
        return GSON.fromJson(reader, type);
    }

    @Override
    protected <T> T reader(Reader reader, Class<T> type) {
        Conditions.nonNull(reader, "reader");
        Conditions.nonNull(type, "type");
        return reader(reader, (Type) type);
    }

    @Override
    protected String serialize(JsonObject object) {
        return GSON.toJson(Conditions.nonNull(object, "object"));
    }

    /** {@inheritDoc} */
    public static class Builder extends AbstractHttpFetcher.AbstractBuilder<JsonHttpFetcher, Builder> {
        private Value<Gson> gson = Value.empty();

        /** The Gson object used for handling JSON requests */
        public Builder gson(Gson gson) {
            this.gson = Value.of(gson);
            return this;
        }

        @Override
        public JsonHttpFetcher build() {
            return new JsonHttpFetcher(
                maxTries.getOrElse(3),
                gson.getOrElse(new GsonBuilder().disableHtmlEscaping().create()),
                executorService.getOrElse(Executors.newCachedThreadPool())
            );
        }
    }
}
