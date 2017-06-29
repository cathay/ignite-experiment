package com.mstage.ignite;


import com.google.code.morphia.annotations.Converters;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import com.mstage.ignite.utils.DateTimeUtils;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity("events")
public class Event implements Serializable{

    /** Required by Morphia. */
    @Id
    private ObjectId objectId;

    @Property("_country")
    private String country;

    @Property("_auth")
    private String auth;
//    private String os;
//    private String osVersion;
//
//    private String profileId;
    @Property("project_id")
    private String projectId;
//    private String name;
//
    @QuerySqlField(index = true)
    private String createdAt;
//    private String ip;
//    private String accountId;
//    private boolean free;
//    private String type;
//    private String isp;
//    private String visitId;

    @QuerySqlField(index = true)
    private transient Date createdAtDate;

    private Event() {

    }

    public Date getCreatedAtDate() {
        if(createdAt == null) {
            return new Date(0);
        }

        if(createdAtDate == null) {
            return DateTimeUtils.parseDate(createdAt);
        }
        return createdAtDate;
    }

    public void setCreatedAtDate(Date createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCreatedAt() {
        System.out.println("getCreatedAt");
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s,%s,%s,%s, %s]",
                objectId, country, auth, createdAt,projectId, getCreatedAtDate());
    }
}
