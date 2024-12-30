package de.gematik.mdrepo;

import de.gematik.mdrepo.model.AdminData;
import de.gematik.mdrepo.model.AppData;
import io.quarkus.redis.client.RedisClientName;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;


@ApplicationScoped
@Data
public class MDService {

    private List<AdminData> adminDataList = new ArrayList<>();
    private List<AppData> appDataList = new ArrayList<>();

    //@Inject @RedisClientName("redis-local-dev") ReactiveRedisDataSource reactiveDS;

    private final ValueCommands<String, AppData> appDataCommands;
    private final ValueCommands<String, AdminData> adminDataCommands;

    public MDService(@RedisClientName("redis-local-dev") RedisDataSource ds) {
        appDataCommands = ds.value(AppData.class);
        adminDataCommands = ds.value(AdminData.class);
    }

    public AppData get(String key) {
        return appDataCommands.get(key);
    }

    public AdminData getAdminData(String key) {
        return adminDataCommands.get(key);
    }

    public void set(String key, AppData appData) {
        appDataCommands.set(key, appData);
    }

    public void set(String key, AdminData adminData) {
        adminDataCommands.set(key, adminData);
    }
}
