package de.gematik.mdrepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.mdrepo.model.*;
import io.quarkus.security.Authenticated;
import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import net.jimblackler.jsonschemafriend.*;

import java.util.*;

@Path("/mdsammler")
@Authenticated
public class AppDataCollectorResource {

    @Inject MDService mdService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add/new-unvalidated")
    public Response addDiscoveryObject(@QueryParam("anbieter") @NonNull String anbieter,
                                       @QueryParam("appname") @NonNull String appName,
                                       @QueryParam("appversion") @NonNull String appVersion,
                                       @NonNull @NotNull AppData appData) {
        String multiKey = "AppDataList:" + anbieter + ":" + appName + ":" + appVersion;
        AppData ad = appData;
        mdService.getAppDataListCommands().rpush(multiKey, appData);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add/new-validated")
    public Response addValidatedBundle(@QueryParam("anbieter") @NonNull String anbieter,
                                       @QueryParam("appname") @NonNull String appName,
                                       @QueryParam("appversion") @NonNull String appVersion,
                                       @QueryParam("schema-id") String schemaId,
                                       @NonNull @NotNull AppData appData) {

        String hashKey = "AdminData-AppData-Bundle:" + anbieter + ":" + appName + ":" + appVersion;
        hashKey = (schemaId != null && schemaId.length() > 0) ? hashKey + ":" + schemaId : hashKey;

        String matchSchema = "admindata";
        AdminData adminData = mdService.getAdminDataHashCommands().hget(hashKey, matchSchema);

        if (adminData == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("AdminData not found for key: " + hashKey).build();
        }

        String schemaStr = adminData.getSchemaStr();
        SchemaStore schemaStore = new SchemaStore();
        Schema schema = null;

        try {
            schema = schemaStore.loadSchemaJson(schemaStr);
            ObjectMapper objectMapper = new ObjectMapper();
            Validator validator = new Validator();
            validator.validateJson(schema, objectMapper.writeValueAsString(appData));
        } catch (ValidationException | GenerationException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String newUuid = appData.getMetaData().getDiscoveryObject().getUuid().toString();
        Map<String, AppData> existingEntries = mdService.getAppDataHashCommands().hgetall(hashKey);

        if (existingEntries != null) {
            for (AppData storedData : existingEntries.values()) {
                if (storedData != null &&
                        storedData.getMetaData() != null &&
                        storedData.getMetaData().getDiscoveryObject() != null) {
                    String storedUuid = storedData.getMetaData().getDiscoveryObject().getUuid().toString();
                    if (newUuid.equals(storedUuid)) {
                        return Response.status(Response.Status.CONFLICT)
                                .entity("Discovery object with UUID " + newUuid + " already exists in this bundle.")
                                .build();
                    }
                }
            }
        }

        String redisFieldUuid = UUID.randomUUID().toString().replace("-","");
        String appDataField = "appdata:" + redisFieldUuid;
        mdService.getAppDataHashCommands().hset(hashKey, appDataField, appData);

        return Response.ok("Discovery object with UUID " + newUuid + " added to this bundle.").build();
    }


    @POST
    @Path("/remove/allbundleobjects")
    public Response removeValidatedBundle(@QueryParam("anbieter") @NonNull String anbieter,
                                          @QueryParam("appname") @NonNull String appName,
                                          @QueryParam("appversion") @NonNull String appVersion,
                                          @QueryParam("schema-id") @NonNull String schemaId) {
        String hashKey = "AdminData-AppData-Bundle:" + anbieter + ":" + appName + ":" + appVersion;
        hashKey = (schemaId != null && !schemaId.isEmpty()) ? hashKey + ":" + schemaId : hashKey;
        Map<String, AppData> allEntries = mdService.getAppDataHashCommands().hgetall(hashKey);

        if (allEntries == null || allEntries.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No data found for that schema.").build();
        }

        List<String> fieldsToDeleteList = new ArrayList<>();

        for (String fieldKey : allEntries.keySet()) {
            if (fieldKey != null && fieldKey.startsWith("appdata:")) {
                fieldsToDeleteList.add(fieldKey);
            }
        }

        if (!fieldsToDeleteList.isEmpty()) {
            String[] fieldsToDeleteArray = fieldsToDeleteList.toArray(new String[0]);
            mdService.getAppDataHashCommands().hdel(hashKey, fieldsToDeleteArray);
            return Response.ok(fieldsToDeleteList.size() + " discovery objects removed.").build();
        } else {
            return Response.ok("No discovery objects found to remove from that schema.").build();
        }
    }

    @POST
    @Path("/remove/bundleobject")
    public Response removeValidatedBundle(@QueryParam("anbieter") @NonNull String anbieter,
                                          @QueryParam("appname") @NonNull String appName,
                                          @QueryParam("appversion") @NonNull String appVersion,
                                          @QueryParam("schema-id") String schemaId,
                                          @QueryParam("object-uuid") String targetUuid,
                                          @QueryParam("object-name") String targetName) {

        boolean hasUuidFilter = (targetUuid != null && !targetUuid.isEmpty());
        boolean hasNameFilter = (targetName != null && !targetName.isEmpty());

        if (!hasUuidFilter && !hasNameFilter) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide at least 'object-uuid' or 'object-name' parameter to delete specific discovery objects.")
                    .build();
        }

        String hashKey = "AdminData-AppData-Bundle:" + anbieter + ":" + appName + ":" + appVersion;
        hashKey = (schemaId != null && !schemaId.isEmpty()) ? hashKey + ":" + schemaId : hashKey;
        Map<String, AppData> allEntries = mdService.getAppDataHashCommands().hgetall(hashKey);

        if (allEntries == null || allEntries.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No data found for that schema.").build();
        }

        List<String> fieldsToDeleteList = new ArrayList<>();

        for (Map.Entry<String, AppData> entry : allEntries.entrySet()) {
            String fieldKey = entry.getKey();
            AppData data = entry.getValue();

            if (fieldKey != null && fieldKey.startsWith("appdata:") && data != null) {
                if (data.getMetaData() != null && data.getMetaData().getDiscoveryObject() != null) {
                    String storedUuid = data.getMetaData().getDiscoveryObject().getUuid().toString();
                    String storedName = data.getMetaData().getDiscoveryObject().getName();
                    boolean match = false;

                    if (hasUuidFilter && targetUuid.equals(storedUuid)) {
                        match = true;
                    }

                    if (hasNameFilter && targetName.equals(storedName)) {
                        match = true;
                    }

                    if (match) {
                        fieldsToDeleteList.add(fieldKey);
                    }
                }
            }
        }

        if (!fieldsToDeleteList.isEmpty()) {
            String[] fieldsToDeleteArray = fieldsToDeleteList.toArray(new String[0]);
            mdService.getAppDataHashCommands().hdel(hashKey, fieldsToDeleteArray);

            return Response.ok(fieldsToDeleteList.size() + " discovery objects removed matching criteria.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No discovery objects found matching the provided UUID or Name.").build();
        }
    }

}