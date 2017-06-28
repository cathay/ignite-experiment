package com.mstage.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteReflectionFactory;
import org.bson.types.ObjectId;

import javax.cache.configuration.FactoryBuilder;

public class ProjectIgniteClient {
    private static final String CACHE_NAME = ProjectIgniteClient.class.getSimpleName();

    public static void main(String []args) {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("examples/config/example-cache.xml")) {

            //IgniteCache<Long, Employee> cache = ignite.getOrCreateCache(cfg);
            IgniteCache<Long, Project> cache = ignite.cache("projects");
            System.out.println(cache);

            System.out.println(cache.get(1L));

            cache.loadCache(null);
            //atomicExample(ignite, cache);
            //System.out.println(cache.get(1L));
           // System.out.println(cache.get(11L));
            //atomicExample(ignite);
            //System.out.println(cache.get(9L));
        }
    }

    /**
     * Atomic example.
     *
     * @param ignite Ignite.
     */
    private static void atomicExample(Ignite ignite, IgniteCache<Long, Project> cache) {
        log(">>>");
        log(">>> Atomic example.");
        log(">>>");

        //IgniteLogger log = ignite.log().getLogger(ProjectIgniteClient.class);

        int cnt = 10;

        for (long i = 1; i <= cnt; i++){
            System.out.println("loop put....");
            cache.put(i, new Project(i,"Name-" + i, i * 1000));
        }
    }

    /**
     * Logs message.
     *
     * @param msg Message to log.
     */
    private static void log(String msg) {
        log(null, msg);
    }

    /**
     * Logs message.
     *
     * @param log Log.
     * @param msg Message to log.
     */
    private static void log(IgniteLogger log, String msg) {
        if (log == null)
            System.out.println(msg);
        else
            log.info(msg);
    }
}
