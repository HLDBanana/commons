package com.yss.datamiddle.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @description: Gson工厂
 * @author:
 * @create: 2020/8/17 13:46
 * @update: 2020/8/17 13:46
 */
public class GsonFactory {

    /**
     * The Constant instance.
     */
    private static final GsonFactory instance = new GsonFactory();

    /**
     * Ge instance.
     *
     * @return the gson factory
     */

    public static GsonFactory geInstance() {
        return instance;
    }


    /**
     * The builder.
     */
    private GsonBuilder builder;

    /**
     * Instantiates a new gson factory.
     */
    private GsonFactory() {
        builder = new GsonBuilder().serializeNulls();
    }

    /**
     * Registry.
     *
     * @param type    the type
     * @param adapter the adapter
     * @return the gson factory
     */
    public GsonFactory registry(Class<?> type, Object adapter) {
        builder.registerTypeAdapter(type, adapter);
        return this;
    }

    /**
     * Creates the.
     *
     * @return the gson
     */
    public Gson create() {
        return builder.create();
    }
}
