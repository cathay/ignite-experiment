package com.mstage.ignite.cache.events;


import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

//FIXME This class must be immutable
@Entity("events")
public class MstageEvent implements Serializable {

    @QuerySqlField(index = true)
    private String id;

    @QuerySqlField(index = true)
    private String country;

    @QuerySqlField(index = true)
    private final Date createdAt;

    @QuerySqlField(index = true)
    private final String name;

    @QuerySqlField
    private final String projectId;

    @QuerySqlField
    private final String profileId;

    private Document document;

    public static final String EVENT_TABLE_NAME = "events";

    public void setCountry(String country) {
        this.country = country;
    }

    public Document getDocument() {
        return document;
    }

    public MstageEvent(String id,
                       String country,
                       Date createdAt,
                       String name,
                       String projectId,
                       String profileId,
                       Document document) {
        this.id = id;
        this.country = country;
        this.createdAt = createdAt;
        this.name = name;
        this.projectId = projectId;
        this.profileId = profileId;
        this.document = document;
    }

    @Override
    public String toString() {
        return String.format("%s:[%s,%s,%s,%s,%s,%s]:%s",
                getClass().getSimpleName(),
                id,
                country,
                createdAt,
                name,
                profileId,
                projectId,
                document);
    }
}
