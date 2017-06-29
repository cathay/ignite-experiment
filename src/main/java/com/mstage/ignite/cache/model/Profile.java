package com.mstage.ignite.cache.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.bson.Document;

import java.util.Date;

public class Profile {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private final String country;

    @QuerySqlField(index = true)
    private final Date createdAt;

    @QuerySqlField
    private String firstName;

    @QuerySqlField
    private String lastName;

    @QuerySqlField
    private Integer age;

    @QuerySqlField
    private final String accountId;

    @QuerySqlField
    private final String projectId;

    private Document document;

    public static final String TABLE_NAME = "profiles";

    public Profile(String id,
                   String country,
                   Date createdAt,
                   String firstName,
                   String lastName,
                   Integer age,
                   String accountId,
                   String projectId,
                   Document document) {
        this.id = id;
        this.country = country;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.accountId = accountId;
        this.projectId = projectId;
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return String.format("%s:[%s,%s,%s,%s,%s,%s,%s,%s]:%s",
                getClass().getSimpleName(),
                id,
                country,
                createdAt,
                firstName,
                lastName,
                age,
                accountId,
                projectId,
                document);
    }
}
