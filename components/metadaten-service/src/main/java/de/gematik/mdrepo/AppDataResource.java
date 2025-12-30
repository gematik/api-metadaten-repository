package de.gematik.mdrepo;

import de.gematik.mdrepo.model.*;
import io.quarkus.redis.datasource.keys.KeyScanArgs;
import io.quarkus.redis.datasource.keys.KeyScanCursor;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;

import java.util.*;

@Path("/mdauskunft")
public class AppDataResource {

    @Inject
    MDService mdService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all/unvalidated")
    public List<AppData> provideInfo(@QueryParam("anbieter") String anbieter,
                                     @QueryParam("appname") String appName,
                                     @QueryParam("appversion") String appVersion) {
        String multiKey = "AppDataList:" + anbieter + ":" + appName + ":" + appVersion;
        return mdService.getAppDataListCommands().lrange(multiKey, 0, -1);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/specific/validated")
    public BundleResult provideInfoBundleSpecific(@QueryParam("anbieter") @NonNull String anbieter,
                                                  @QueryParam("appname") @NonNull  String appName,
                                                  @QueryParam("appversion") @NonNull String appVersion,
                                                  @QueryParam("schema-id") @NonNull String schemaId) {
        return this.provideInfoBundle(anbieter, appName, appVersion, schemaId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all/validated")
    public BundleResult provideInfoBundle(@QueryParam("anbieter") @NonNull String anbieter,
                                          @QueryParam("appname") @NonNull  String appName,
                                          @QueryParam("appversion") @NonNull String appVersion,
                                          String schemaId) {

        BundleResult result = new BundleResult();
        String basicHashKey = "AdminData-AppData-Bundle:" + anbieter + ":" + appName + ":" + appVersion;

        Set<String> keysToFetch = new HashSet<>();

        result.setSchemaIds(new ArrayList<>());
        result.setMatchingAppData(new ArrayList<>());

        if (schemaId != null && !schemaId.isEmpty()) {
            String specificKey = basicHashKey + ":" + schemaId;
            keysToFetch.add(specificKey);
            result.getSchemaIds().add(schemaId);
        }
        else {
            KeyScanArgs keyScanArgs = new KeyScanArgs();
            keyScanArgs.match(basicHashKey + ":*");
            KeyScanCursor<String> cursor = mdService.getKeyCommands().scan(keyScanArgs);

            while (cursor.hasNext()) {
                Set<String> foundKeys = cursor.next();

                for (String fullKey : foundKeys) {
                    keysToFetch.add(fullKey);

                    if (fullKey.length() > basicHashKey.length()) {
                        int lastIndex = fullKey.lastIndexOf(":");

                        if (lastIndex != -1) {
                            String cleanSchemaId = fullKey.substring(lastIndex + 1);
                            result.getSchemaIds().add(cleanSchemaId);
                        }
                    }
                }
            }
        }

        for (String hashKey : keysToFetch) {
            Map<String, AppData> map = mdService.getAppDataHashCommands().hgetall(hashKey);

            if (map != null) {
                for (Map.Entry<String, AppData> entry : map.entrySet()) {
                    String fieldKey = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof AppData && fieldKey != null && fieldKey.startsWith("appdata:")) {
                        result.getMatchingAppData().add((AppData) value);
                    }
                }
            }
        }

        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/specific/by-name/{objname}")
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


