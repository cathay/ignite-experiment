package com.mstage.ignite;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.converters.DefaultConverters;
import com.mongodb.MongoClient;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.apache.ignite.resources.LoggerResource;
import org.bson.types.ObjectId;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class EventCacheStore extends CacheStoreAdapter<String, Event> implements LifecycleAware {
    /** MongoDB port. */
    private static final int MONGOD_PORT = 27017;

    /** Mongo data store. */
    private Datastore morphia;

    /** Logger. */
    @LoggerResource
    private IgniteLogger log;

    /** {@inheritDoc} */
    @Override public void start() throws IgniteException {

        System.out.println("EventCacheStore.......");

        MongoClient mongo = new MongoClient("127.0.0.1", MONGOD_PORT);

        Set<Class> clss = new HashSet<>();
        Collections.addAll(clss, Event.class);

        System.out.println("Morphia started...");
        morphia = new Morphia(clss).createDatastore(mongo, "test");
    }

    /** {@inheritDoc} */
    @Override
    public void stop() throws IgniteException {
    }

    /** {@inheritDoc} */
    @Override
    public Event load(String key) throws CacheLoaderException {
        System.out.println("load:" + key + ":" + key.getClass());

        Event e = morphia.find(Event.class).field("objectId").equal(new ObjectId(key)).get();

        System.out.println("before return..." +e);

        return e;
    }

    /** {@inheritDoc} */
    @Override
    public void write(Cache.Entry<? extends String, ? extends Event> e) throws CacheWriterException {
        System.out.println("write:" + e.getKey() + ":" + e.getKey().getClass());
        morphia.save(e.getValue());
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Object key) throws CacheWriterException {

        Event e = morphia.find(Event.class).field("objectId").equal(new ObjectId((String)key)).get();

        if (e != null) {
            morphia.delete(e);
        }
    }

    /**
     * @param msg Message.
     */
    private void log(String msg) {
        if (log != null) {
            log.info(">>>");
            log.info(">>> " + msg);
            log.info(">>>");
        }
        else {
            System.out.println(">>>");
            System.out.println(">>> " + msg);
            System.out.println(">>>");
        }
    }
}
