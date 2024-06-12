package de.gematik.mdrepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.mdrepo.model.*;
import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import net.jimblackler.jsonschemafriend.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Path("/mdsammler")
public class AppDataCollectorResource {

    @Inject MDService mdService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/listall")
    public List<AppData> provideInfo(@QueryParam("anbieter") String anbieter,
                               @QueryParam("appname") String appName,
                               @QueryParam("appversion") String appVersion) {
        return mdService.getAppDataList();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDiscoveryObject(@QueryParam("anbieter") @NonNull String anbieter,
                                       @QueryParam("appname") @NonNull String appName,
                                       @QueryParam("appversion") @NonNull String appVersion,
                                       @NonNull @NotNull AppData appData) {

        for (ListIterator adl = mdService.getAppDataList().listIterator(); adl.hasNext();) {
            AppData a = (AppData)adl.next();
            if (a.getMetaData().getDiscoveryObject().getName().equals(appData.getMetaData().getDiscoveryObject().getName()) &&
                a.getMetaData().getDiscoveryObject().getUuid().equals((appData.getMetaData().getDiscoveryObject().getUuid()))) {
                return Response.notModified().build();
            }
        }

        for (ListIterator adminDataList = mdService.getAdminDataList().listIterator(); adminDataList.hasNext();) {
            AdminData adminData = (AdminData)adminDataList.next();
            if (adminData.getMasterData().getAnbieter().equals(anbieter) &&
                adminData.getMasterData().getAppName().equals(appName) &&
                adminData.getMasterData().getAppVersion().equals(appVersion)) {

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
            }
        }
        mdService.getAppDataList().add(appData);
        return Response.ok().build();
    }

    //TODO Update bestehendes Obj (PUT)

}