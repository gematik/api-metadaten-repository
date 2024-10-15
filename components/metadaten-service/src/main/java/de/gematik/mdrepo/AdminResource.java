package de.gematik.mdrepo;

import de.gematik.mdrepo.model.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;

import java.util.ArrayList;
import java.util.List;

@Path("/admin/app")
public class AdminResource {

    @Inject MDService mdService;

//    @POST
//    @Consumes(MediaType.TEXT_PLAIN)
//    @Path("/validationscheme")
//    public Response addSchema(@QueryParam("anbieter") @NonNull String anbieter,
//                              @QueryParam("appname") @NonNull String appName,
//                              @QueryParam("appversion") @NonNull String appversion,
//                              String schemaStr) {
//        MasterData masterData = new MasterData(anbieter, appName, appversion);
//        SchemaStore schemaStore = new SchemaStore();
//
//        try {
//            schemaStore.loadSchemaJson(schemaStr);
//            mdService.getAdminDataList().clear();
//            mdService.getAdminDataList().add(new AdminData(masterData, schemaStr));
//        } catch (GenerationException e) {
//            throw new RuntimeException(e);
//        }
//        return Response.ok().build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdminData> provideInfo(@QueryParam("anbieter") @NonNull String anbieter,
                                 @QueryParam("appname") @NonNull String appName,
                                 @QueryParam("appversion") @NonNull String appversion) {
        return mdService.getAdminDataList();
    }

}


