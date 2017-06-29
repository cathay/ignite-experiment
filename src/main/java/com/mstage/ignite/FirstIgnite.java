package com.mstage.ignite;


import javax.cache.Cache;
import javax.cache.configuration.FactoryBuilder;

import com.mstage.ignite.cache.events.MstageEvent;
import com.mstage.ignite.cache.store.MstageEventCacheStore;
import com.mstage.ignite.cache.store.EventCacheStore;
import com.mstage.ignite.utils.DateTimeUtils;
import org.apache.ignite.*;
import org.apache.ignite.cache.*;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.*;

import java.text.ParseException;

public class FirstIgnite {

    public static void main(String []args) throws ParseException{

        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("config/cache.xml")) {

            //IgniteCache<String, Event> events = ignite.getOrCreateCache(createEventStoreCfg("events"));
            IgniteCache<String, MstageEvent> events = ignite.getOrCreateCache(createContentConsumedEventStoreCfg("events"));
            //IgniteCache<String, Event> events = ignite.cache("events");

            //String id ="5953325e119ab725e67d8799";
            String id ="595474a8fc5992486c0e987d";
            MstageEvent event = events.get(id);
            System.out.println(event);

            //FIXME Don't try on staging data
            //event.setCountry("VN");
            //events.getAndReplace(id, event);
           // events.put("595474a8fc5992486c0e987d", event);
            System.out.println("............");

            SqlQuery sql = new SqlQuery<Long,MstageEvent>(MstageEvent.class, "createdAt = ?");

            try (QueryCursor<Cache.Entry<Long, Event>> cursor = events.query(sql.setArgs(DateTimeUtils.parseDate("2017-06-29T03:31:46.989Z")))) {
                for (Cache.Entry<Long, Event> e : cursor)
                    System.out.println(e.getValue());
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //FIXME Move these to abstract class
    public static CacheConfiguration createEventStoreCfg(String cacheName) {
        CacheConfiguration<String, Event> cfg = new CacheConfiguration<>();

        cfg.setName(cacheName);
        cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cfg.setCacheStoreFactory(FactoryBuilder.factoryOf(EventCacheStore.class));
        cfg.setIndexedTypes(String.class, Event.class);
        cfg.setWriteThrough(true);
        cfg.setReadThrough(true);
        return cfg;
    }

    public static CacheConfiguration createContentConsumedEventStoreCfg(String cacheName) {
        CacheConfiguration<String, MstageEvent> cfg = new CacheConfiguration<>();

        cfg.setName(cacheName);
        cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cfg.setCacheStoreFactory(FactoryBuilder.factoryOf(MstageEventCacheStore.class));
        cfg.setIndexedTypes(String.class, MstageEvent.class);
        cfg.setWriteThrough(true);
        cfg.setWriteBehindEnabled(true);
        cfg.setReadThrough(true);
        return cfg;
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
