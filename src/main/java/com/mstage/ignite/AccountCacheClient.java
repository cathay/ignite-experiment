package com.mstage.ignite;


import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;

public class AccountCacheClient {

    public static void main(String []args) {
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start("examples/config/example-ignite.xml")) {
            CacheConfiguration<Long, Account> cfg = new CacheConfiguration<>();
            cfg.setName("Account");
            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

            IgniteCache<Long, Account> cache = ignite.getOrCreateCache(cfg);

            System.out.println("okie");
            System.out.println(cache.get(1L));

            cache.put(1L, new Account(100,1));
            cache.put(2L, new Account(200,2));

            System.out.println(cache.get(1L));
        }
    }
}
