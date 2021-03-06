package com.mstage.ignite.cache.store;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mstage.ignite.cache.model.MstageEvent;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.apache.ignite.resources.LoggerResource;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mstage.ignite.cache.model.MstageEvent.TABLE_NAME;


public class MstageEventCacheStore extends CacheStoreAdapter<String, MstageEvent> implements LifecycleAware {
    /** MongoDB port. */
    private static final int MONGOD_PORT = 27017;

    /** Logger. */
    @LoggerResource
    private IgniteLogger log;

    private MongoClient mongoClient;
    private MongoDatabase db;

    /** {@inheritDoc} */
    @Override public void start() throws IgniteException {

        System.out.println("MstageEventCacheStore.......");

        MongoClientURI uri = new MongoClientURI("mongodb://userkit-staging:userkit-pwd@54.169.112.195:27017/userkit");
        //mongoClient = new MongoClient("127.0.0.1", MONGOD_PORT);
        //db = mongoClient.getDatabase("test");
        mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase("userkit");

        Set<Class> clss = new HashSet<>();
        Collections.addAll(clss, MstageEvent.class);
    }

    /** {@inheritDoc} */
    @Override
    public void stop() throws IgniteException {
    }

    @Override
    public void loadCache(final IgniteBiInClosure<String, MstageEvent> clo, Object... args) {
        if (args == null || args.length == 0 || args[0] == null)
            throw new CacheLoaderException("Expected a limit months of data is not provided.");

        int month = (Integer)args[0];

        Date now = new Date();
        Date fromDate = new DateTime().minusMonths(month).toDate();

        System.out.println(now);
        System.out.println(fromDate);

        BasicDBObject query = new BasicDBObject();
        query.put("createdAt", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", now));

        System.out.println("loadCache....");
        
        //TODO Need to use lamda with Java-8
        getCollection().find(query).map(new Function<Document, MstageEvent>() {

            @Override
            public MstageEvent apply(Document document) {
               MstageEvent event =  createEvent(document);
               System.out.println(event);
               clo.apply(event.getId(), event);
               return event;
            }
        });


    }

    /** {@inheritDoc} */
    @Override
    public MstageEvent load(String key) throws CacheLoaderException {
        System.out.println("..........");
        System.out.println(findById(key));
        System.out.println("..........");

        return createEvent(findById(key));
    }

    private static MstageEvent createEvent(Document document) {
        if(document == null)
            return null;

        //TODO Need to exact field names for maintenance purpose
        MstageEvent event = new MstageEvent(
                document.get("_id").toString(),
                document.getString("_country"),
                document.getDate("createdAt"),
                document.getString("name"),
                document.get("project_id").toString(),
                document.get("profile_id").toString(),
                document
        );

        return event;
    }

    private MongoCollection<Document> getCollection() {
        return db.getCollection(TABLE_NAME);
    }

    private Document findById(String key) {
        return getCollection().find(eq("_id", new ObjectId(key))).first();
    }

    /** {@inheritDoc} */
    @Override
    public void write(Cache.Entry<? extends String, ? extends MstageEvent> e) throws CacheWriterException {

        System.out.println(".......write:"+ e.getValue().getDocument());
        Document document = findById(e.getKey());

        if(document == null){
            getCollection().insertOne(e.getValue().getDocument());
            return;
        }
        System.out.println("remove _id:"+e.getValue().getDocument().remove("_id"));
        System.out.println("current:" + document);

        //FIXME Need to fix update case => Exception because of _id
        getCollection().updateOne(document, e.getValue().getDocument());
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Object key) throws CacheWriterException {

        System.out.println("...delete events:" + key);
        Document document = findById((String)key);

        if (document != null) {
            getCollection().deleteOne(document);
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
