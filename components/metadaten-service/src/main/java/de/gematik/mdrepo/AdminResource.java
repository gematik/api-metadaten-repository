package de.gematik.mdrepo;

import de.gematik.mdrepo.model.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.NonNull;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import java.util.UUID;

@Path("/admin/app")
public class AdminResource {

    @Inject MDService mdService;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add/validationscheme")
    public BundleUUID addSchema(@QueryParam("anbieter") @NonNull String anbieter,
                              @QueryParam("appname") @NonNull String appName,
                              @QueryParam("appversion") @NonNull String appVersion,
                              String schemaStr) {

        String uuid = UUID.randomUUID().toString().replace("-","");
        String hashKey = "AdminData-AppData-Bundle:" + anbieter + ":" + appName + ":" + appVersion + ":" + uuid;
        String field = "admindata";
        MasterData masterData = new MasterData(anbieter, appName, appVersion);
        SchemaStore schemaStore = new SchemaStore();

        try {
            schemaStore.loadSchemaJson(schemaStr);
            mdService.getAdminDataHashCommands().hset(hashKey, field, new AdminData(masterData, schemaStr));
        } catch (GenerationException e) {
            throw new RuntimeException(e);
        }

        return new BundleUUID(uuid);
    }

}


