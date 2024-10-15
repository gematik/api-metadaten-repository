package de.gematik.mdrepo;

import de.gematik.mdrepo.db.MasterDataRepository;
import de.gematik.mdrepo.model.MasterData;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/MasterData")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasterDataResource {
    @Inject
    MasterDataRepository masterDataRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MasterData> listAnbieter(@QueryParam("anbieter") String anbieter, @QueryParam("appName") String appName) {
        if (anbieter == null) {
            return masterDataRepository.listAll();
        }
        return masterDataRepository.findByAnbieter(anbieter);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void addMasterData(MasterData masterData) {
           masterDataRepository.persist(masterData);
    }
}
