package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {

    private final Map<Long, User> users =
            new ConcurrentHashMap<>(); // concurrent so that it's thread safe (since a servlet can handle concurrent requests)

    private final AtomicLong idGenerator =
            new AtomicLong(1);

    public User getUser(Long id) {
        return users.get(id);
    }

    public User createUser(String name) {

        Long id = idGenerator.getAndIncrement();
        User user = new User(id, name);

        users.put(id, user);

        return user;
    }


    public User updateUser(Long id, String name) {
        User updated = new User(id, name);

        // makes it atomic so that race condition does not allow for deletion of user during an update
        return users.computeIfPresent(
                id,
                (key, existing) -> updated
        );
    }

    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }
}
