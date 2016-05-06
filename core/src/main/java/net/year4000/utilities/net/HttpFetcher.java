/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import net.year4000.utilities.Callback;

import java.lang.reflect.Type;

public interface HttpFetcher<D> {
    /** HTTP get method with async request */
    default <T> void get(HttpConnection url, Class<T> clazz, Callback<T> callback) {
        get(url, (Type) clazz, callback);
    }

    /** HTTP get method with async request */
    default <T> void get(String url, Class<T> clazz, Callback<T> callback) {
        get(new HttpConnection(url), clazz, callback);
    }

    /** HTTP get method with async request */
    <T> void get(HttpConnection url, Type type, Callback<T> callback);

    /** HTTP get method with async request */
    default <T> void get(String url, Type type, Callback<T> callback) {
        get(new HttpConnection(url), type, callback);
    }

    /** HTTP get method with sync request */
    default <T> T get(HttpConnection url, Class<T> clazz) throws Exception {
        return get(url, (Type) clazz);
    }

    /** HTTP get method with sync request */
    default <T> T get(String url, Class<T> clazz) throws Exception {
        return get(new HttpConnection(url), clazz);
    }

    /** HTTP get method with sync request */
    <T> T get(HttpConnection url, Type type) throws Exception;

    /** HTTP get method with sync request */
    default <T> T get(String url, Type type) throws Exception {
        return get(new HttpConnection(url), type);
    }

    /** HTTP post method with async request */
    default <T> void post(HttpConnection url, D data, Class<T> clazz, Callback<T> callback) {
        post(url, data, (Type) clazz, callback);
    }

    /** HTTP post method with async request */
    default <T> void post(String url, D data, Class<T> clazz, Callback<T> callback) {
        post(new HttpConnection(url), data, clazz, callback);
    }

    /** HTTP post method with async request */
    <T> void post(HttpConnection url, D data, Type type, Callback<T> callback);

    /** HTTP post method with async request */
    default <T> void post(String url, D data, Type type, Callback<T> callback) {
        post(new HttpConnection(url), data, type, callback);
    }

    /** HTTP post method with sync request */
    default <T> T post(HttpConnection url, D data, Class<T> clazz) throws Exception {
        return post(url, data, (Type) clazz);
    }

    /** HTTP post method with sync request */
    default <T> T post(String url, D data, Class<T> clazz) throws Exception {
        return post(new HttpConnection(url), data, clazz);
    }

    /** HTTP post method with sync request */
    <T> T post(HttpConnection url, D data, Type type) throws Exception;

    /** HTTP post method with sync request */
    default <T> T post(String url, D data, Type type) throws Exception {
        return post(new HttpConnection(url), data, type);
    }

    /** HTTP put method with async request */
    default <T> void put(HttpConnection url, D data, Class<T> clazz, Callback<T> callback) {
        put(url, data, (Type) clazz, callback);
    }

    /** HTTP put method with async request */
    default <T> void put(String url, D data, Class<T> clazz, Callback<T> callback) {
        put(new HttpConnection(url), data, clazz, callback);
    }

    /** HTTP put method with async request */
    <T> void put(HttpConnection url, D data, Type type, Callback<T> callback);

    /** HTTP put method with async request */
    default <T> void put(String url, D data, Type type, Callback<T> callback) {
        put(new HttpConnection(url), data, type, callback);
    }

    /** HTTP put method with sync request */
    default <T> T put(HttpConnection url, D data, Class<T> clazz) throws Exception {
        return put(url, data, (Type) clazz);
    }

    /** HTTP put method with sync request */
    default  <T> T put(String url, D data, Class<T> clazz) throws Exception {
        return put(new HttpConnection(url), data, clazz);
    }

    /** HTTP put method with sync request */
    <T> T put(HttpConnection url, D data, Type type) throws Exception;

    /** HTTP put method with sync request */
    default <T> T put(String url, D data, Type type) throws Exception {
        return put(new HttpConnection(url), data, type);
    }

    /** HTTP delete method with async request */
    default <T> void delete(HttpConnection url, D data, Class<T> clazz, Callback<T> callback) {
        delete(url, data, (Type) clazz, callback);
    }

    /** HTTP delete method with async request */
    default <T> void delete(String url, D data, Class<T> clazz, Callback<T> callback) {
        delete(new HttpConnection(url), data, clazz, callback);
    }

    /** HTTP delete method with async request */
    <T> void delete(HttpConnection url, D data, Type type, Callback<T> callback);

    /** HTTP delete method with async request */
    default <T> void delete(String url, D data, Type type, Callback<T> callback) {
        delete(new HttpConnection(url), data, type, callback);
    }

    /** HTTP delete method with sync request */
    default <T> T delete(HttpConnection url, D data, Class<T> clazz) throws Exception {
        return delete(url, data, (Type) clazz);
    }

    /** HTTP delete method with sync request */
    default <T> T delete(String url, D data, Class<T> clazz) throws Exception {
        return delete(new HttpConnection(url), data, clazz);
    }

    /** HTTP delete method with sync request */
    <T> T delete(HttpConnection url, D data, Type type) throws Exception;

    /** HTTP delete method with sync request */
    default <T> T delete(String url, D data, Type type) throws Exception {
        return delete(new HttpConnection(url), data, type);
    }

    /** The methods that are current accepted for HttpFetcher */
    enum Methods {GET, POST, PUT, DELETE}
}
