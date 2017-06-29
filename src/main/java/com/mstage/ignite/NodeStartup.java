package com.mstage.ignite;


import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

public class NodeStartup {
    public static void main(String []args) {

        Ignite ignite = Ignition.start("examples/config/cache.xml");
        IgniteCache<String, Event> events = ignite.cache("events");
        events.loadCache(null);
    }
}