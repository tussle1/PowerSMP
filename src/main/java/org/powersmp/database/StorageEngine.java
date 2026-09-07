package org.powersmp.database;

import org.powersmp.model.UserData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StorageEngine {

    void init();

    CompletableFuture<UserData> loadUser(UUID uuid, String username);

    CompletableFuture<Void> saveUser(UserData userData);

    void shutdown();
}