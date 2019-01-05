package com.boot.ignite.bootignite.service;

import com.boot.ignite.bootignite.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private CacheManager cacheManager;

    public User getUser() {
        Cache cache = cacheManager.getCache("boot_ignite_cache");
        User user;
        if (cache != null) {
            user = cache.get("user_data", User.class);
            if (user == null) {
                user = new User("sumit", "sumit.s7325@gmail.com");
                cache.put("user_data", user);
            }
            return user;
        }

        System.out.println(cache);
        return new User("sumit", "sumit.s7325@gmail.com");
    }

}
