package com.mstage.ignite.cache.store;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mstage.ignite.cache.model.Profile;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.apache.ignite.resources.LoggerResource;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mstage.ignite.cache.model.Profile.TABLE_NAME;

//FIXME This class should refactored along with <class>MstageEventCacheStore</class>
public class ProfileCacheStore extends CacheStoreAdapter<String, Profile> implements LifecycleAware {
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
        mongoClient = new MongoClient(uri);
        db = mongoClient.getDatabase("userkit");

        Set<Class> clss = new HashSet<>();
        Collections.addAll(clss, Profile.class);
    }

    /** {@inheritDoc} */
    @Override
    public void stop() throws IgniteException {
    }

    /** {@inheritDoc} */
    @Override
    public Profile load(String key) throws CacheLoaderException {
        System.out.println("..........");
        System.out.println(findById(key));
        System.out.println("..........");

        return createProfile(findById(key));
    }

    private static Profile createProfile(Document document) {
        if(document == null)
            return null;

        //TODO Need to exact field names for maintenance purpose
        Profile profile = new Profile(
                document.get("_id").toString(),
                document.getString("_country"),
                document.getDate("createdAt"),
                document.getString("firstName"),
                document.getString("lastName"),
                document.getInteger("age"),
                document.get("account_id").toString(),
                document.get("project_id").toString(),
                document
        );

        return profile;
    }

    private MongoCollection<Document> getCollection() {
        return db.getCollection(TABLE_NAME);
    }

    private Document findById(String key) {
        return getCollection().find(eq("_id", new ObjectId(key))).first();
    }

    /** {@inheritDoc} */
    @Override
    public void write(Cache.Entry<? extends String, ? extends Profile> e) throws CacheWriterException {

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
