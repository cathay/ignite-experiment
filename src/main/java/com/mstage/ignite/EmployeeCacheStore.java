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
import org.apache.ignite.lifecycle.LifecycleAware;
import org.apache.ignite.resources.LoggerResource;
import org.bson.types.ObjectId;

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
public class EmployeeCacheStore extends CacheStoreAdapter<String, Employee> implements LifecycleAware {
    /** MongoDB port. */
    private static final int MONGOD_PORT = 27017;

    /** Mongo data store. */
    private Datastore morphia;

    /** Logger. */
    @LoggerResource
    private IgniteLogger log;

    /** {@inheritDoc} */
    @Override public void start() throws IgniteException {

        System.out.println("EmployeeCacheStore.......");

        MongoClient mongo = new MongoClient("127.0.0.1", MONGOD_PORT);

        Set<Class> clss = new HashSet<>();
        Collections.addAll(clss, Employee.class);

        System.out.println("Morphia started...");
        morphia = new Morphia(clss).createDatastore(mongo, "test");

    }

    /** {@inheritDoc} */
    @Override
    public void stop() throws IgniteException {
    }

    /** {@inheritDoc} */
    @Override
    public Employee load(String key) throws CacheLoaderException {
        System.out.println("load:" + key + ":" + key.getClass());

        Employee e = morphia.find(Employee.class).field("objectId").equal(new ObjectId(key)).get();

        System.out.println("before return..." +e);

        return e;
    }

    /** {@inheritDoc} */
    @Override
    public void write(Cache.Entry<? extends String, ? extends Employee> e) throws CacheWriterException {
        System.out.println("write:" + e.getKey() + ":" + e.getKey().getClass());
        morphia.save(e.getValue());
    }

    /** {@inheritDoc} */
    @Override
    public void delete(Object key) throws CacheWriterException {

        Employee e = morphia.find(Employee.class).field("objectId").equal(new ObjectId((String)key)).get();

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
