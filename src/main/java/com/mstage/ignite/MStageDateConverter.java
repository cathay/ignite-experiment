package com.mstage.ignite;

import com.google.code.morphia.converters.TypeConverter;
import com.google.code.morphia.mapping.MappedField;
import com.google.code.morphia.mapping.MappingException;
import com.mongodb.BasicDBObject;

import java.util.Calendar;
import java.util.Date;

public class MStageDateConverter extends TypeConverter {

    @Override
    public Object decode(Class targetClass, Object fromDBObject, MappedField mappedField) throws MappingException {
        System.out.println(((BasicDBObject) fromDBObject).getString("createdAt"));
        return Calendar.getInstance().getTime();
    }
}
