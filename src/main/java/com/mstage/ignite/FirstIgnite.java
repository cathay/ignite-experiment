package com.mstage.ignite;


import javax.cache.Cache;
import javax.cache.configuration.FactoryBuilder;
import org.apache.ignite.*;
import org.apache.ignite.cache.*;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.*;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FirstIgnite {

    public static void main(String []args) throws ParseException{

        Ignition.setClientMode(true);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        System.out.println(sdfDate.parse("2017-06-21T07:54:12.469Z").equals(sdfDate.parse("2017-06-21T07:54:12.469Z")));
        try (Ignite ignite = Ignition.start("config/cache.xml")) {

            CacheConfiguration<String, Event> cfg = new CacheConfiguration<>();

            cfg.setName("events");
            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
            cfg.setCacheStoreFactory(FactoryBuilder.factoryOf(EventCacheStore.class));
            cfg.setIndexedTypes(String.class, Event.class);
            cfg.setWriteThrough(true);
            cfg.setReadThrough(true);


            //IgniteCache<String, Event> events = ignite.getOrCreateCache(cfg);
            IgniteCache<String, Event> events = ignite.cache("events");

            //System.out.println(events.get("59533286119ab725e67d87ac").getCreatedAtDate().equals(sdfDate.parse("2017-06-21T07:54:12.469Z")));
            System.out.println(events.get("59546f81fc5992486c0e9872"));
            System.out.println(events.get("5954629cfc5992486c0e9855"));
            System.out.println("............");

            SqlQuery sql = new SqlQuery<Long,Event>(Event.class, "createdAtDate = ?");

            try (QueryCursor<Cache.Entry<Long, Event>> cursor = events.query(sql.setArgs(sdfDate.parse("2017-06-21T07:54:12.469Z")))) {
                for (Cache.Entry<Long, Event> e : cursor)
                    System.out.println(e.getValue());
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void sqlQuery() {

    }

    /**
     * Prints message and query results.
     *
     * @param msg Message to print before all objects are printed.
     * @param col Query results.
     */
    private static void print(String msg, Iterable<?> col) {
        print(msg);
        print(col);
    }

    /**
     * Prints message.
     *
     * @param msg Message to print before all objects are printed.
     */
    private static void print(String msg) {
        System.out.println();
        System.out.println(">>> " + msg);
    }

    /**
     * Prints query results.
     *
     * @param col Query results.
     */
    private static void print(Iterable<?> col) {
        for (Object next : col)
            System.out.println(">>>     " + next);
    }
}
