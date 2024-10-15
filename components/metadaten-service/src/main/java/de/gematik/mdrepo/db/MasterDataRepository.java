package de.gematik.mdrepo.db;

import de.gematik.mdrepo.model.MasterData;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MasterDataRepository implements PanacheRepository<MasterData> {
    public List<MasterData> findByAnbieter(String anbieter) {
        return find("anbieter", anbieter).list();
    }
    public List<MasterData> findByAppName(String appName) {
        return find("appName", appName).list();
    }
}
