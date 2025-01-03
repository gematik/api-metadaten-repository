package de.gematik.mdrepo;

import de.gematik.mdrepo.model.AdminData;
import de.gematik.mdrepo.model.AppData;
import io.quarkus.redis.client.RedisClientName;
import io.quarkus.redis.datasource.hash.HashCommands;
import io.quarkus.redis.datasource.list.ListCommands;
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
    private final ListCommands<String, AppData> appDataListCommands;
    private final HashCommands<String, String, AppData> appDataHashCommands;
    private final ValueCommands<String, AdminData> adminDataCommands;
    private final HashCommands<String, String, AdminData> adminDataHashCommands;
    private final KeyCommands<String> keyCommands;

    public MDService(@RedisClientName("redis-local-dev") RedisDataSource ds) {
        appDataCommands = ds.value(AppData.class);
        appDataListCommands = ds.list(AppData.class);
        appDataHashCommands = ds.hash(AppData.class);
        adminDataCommands = ds.value(AdminData.class);
        adminDataHashCommands = ds.hash(AdminData.class);
        keyCommands = ds.key();
    }

}
