package com.yss.datamiddle.constants;

public enum ServiceEnum {

    QUERY("query"), MODELENGINE("modelEngine"), QUERYENGINE("queryEngine");

    private String name;

    ServiceEnum(String name) {
        this.name = name;
    }

    public static ServiceEnum get(String name) {
        for (ServiceEnum c : ServiceEnum.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public String getName() { return this.name; };
}
