package de.gematik.mdrepo;

import de.gematik.mdrepo.model.AppData;
import de.gematik.mdrepo.model.DiscoveryObject;
import de.gematik.mdrepo.model.MasterData;
import de.gematik.mdrepo.model.MetaData;
import io.quarkus.redis.datasource.hash.HashScanCursor;
import io.quarkus.redis.datasource.keys.KeyScanArgs;
import io.quarkus.redis.datasource.keys.KeyScanCursor;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/mdauskunft")
public class AppDataResource {


    @Inject
    MDService mdService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listall")
    public List<AppData> provideInfo(@QueryParam("anbieter") String anbieter,
                                     @QueryParam("appname") String appName,
                                     @QueryParam("appversion") String appVersion) {
        String multiKey = "AppDataList:" + anbieter + ":" + appName + ":" + appVersion;
        return mdService.getAppDataListCommands().lrange(multiKey, 0, -1);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listall2")
    public List<AppData> provideInfo2(@QueryParam("anbieter") String anbieter,
                                     @QueryParam("appname") String appName,
                                     @QueryParam("appversion") String appVersion,
                                     @QueryParam("schema-id") String schemaId) {
        String basicHashKey = "AdminData-AppData-Bundle:" + anbieter + ":" + appName + ":" + appVersion;
        String hashKey = basicHashKey + ":" + schemaId;
        List entries = new ArrayList<AppData>();

        KeyScanArgs keyScanArgs = new KeyScanArgs();
        keyScanArgs.match(basicHashKey + ":*");
        KeyScanCursor<String> cursor = mdService.getKeyCommands().scan(keyScanArgs);
        Set<String> matchingSet = new HashSet<>();

        while (cursor.hasNext()) {
            Set<String> keys = cursor.next();
            matchingSet.addAll(keys);
        }

        for (String s : matchingSet) {
            Map<String, AppData> map = mdService.getAppDataHashCommands().hgetall(s);
            for (Map.Entry<String, AppData> entry : map.entrySet()) {
                if (entry.getValue() instanceof AppData) {
                    entries.add(entry.getValue());
                }
            }
        }
        return entries;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/specific/{objname}")
    public AppData provideInfo(@QueryParam("anbieter") String anbieter,
                               @QueryParam("appname") String appName,
                               @QueryParam("appversion") String appversion,
                               @PathParam("objname") String objName) {
        //TODO
        if (objName.equals("MyDiscoveryObj")) {
            return new AppData(new MasterData(anbieter, appName, appversion), new MetaData(new DiscoveryObject()));
        } else {
            return new AppData(new MasterData(anbieter, appName, appversion), new MetaData(null));
        }
    }

}


