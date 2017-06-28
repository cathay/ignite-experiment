/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package com.mstage.ignite;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.apache.ignite.resources.LoggerResource;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sample MongoDB embedded cache store.
 *
 * @author @java.author
 * @version @java.version
 */
public class ProjectCacheStore extends CacheStoreAdapter<Long, Project> implements LifecycleAware {
    /** MongoDB port. */
    private static final int MONGOD_PORT = 27017;

    /** Mongo data store. */
    private Datastore morphia;

    /** Logger. */
    @LoggerResource
    private IgniteLogger log;

    /** {@inheritDoc} */
    @Override public void start() throws IgniteException {

        MongoClient mongo = new MongoClient("127.0.0.1", MONGOD_PORT);

        Set<Class> clss = new HashSet<>();

        Collections.addAll(clss, Project.class);
        System.out.println("ProjectCacheStore started...");
        morphia = new Morphia(clss).createDatastore(mongo, "test");


    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Project> clo, Object... args) {
        System.out.println(clo);
        System.out.println(args.length);
    }

    /** {@inheritDoc} */
    @Override public void stop() throws IgniteException {
        System.out.println("Stop...");
    }

    /** {@inheritDoc} */
    @Override
    public Project load(Long key) throws CacheLoaderException {
        System.out.println("load");
        Project e = morphia.createQuery(Project.class).field("id").equal(key).get();

        System.out.println("Loaded employee: " + e);

        return e;
    }

    /** {@inheritDoc} */
    @Override public void write(Cache.Entry<? extends Long, ? extends Project> e) throws CacheWriterException {
        System.out.println("before write:" + e);
        morphia.save(e.getValue());
        System.out.println("write employee");
        System.out.println("Stored employee: " + e.getValue());
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Object key) throws CacheWriterException {
        Project e = morphia.find(Project.class).field("id").equal(key).get();

        if (e != null) {
            morphia.delete(e);

            System.out.println("Removed employee: " + key);
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
