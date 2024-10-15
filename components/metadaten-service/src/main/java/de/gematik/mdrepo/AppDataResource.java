//package de.gematik.mdrepo;
//
//import de.gematik.mdrepo.model.AppData;
//import de.gematik.mdrepo.model.DiscoveryObject;
//import de.gematik.mdrepo.model.MasterData;
//import de.gematik.mdrepo.model.MetaData;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Path("/mdauskunft")
//public class AppDataResource {
//
//    private static final List<AppData> appDataList = new ArrayList<>();
//    private static final List<DiscoveryObject> discoveryObjectList = new ArrayList<>();
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public AppData provideInfo(@QueryParam("anbieter") String anbieter,
//                               @QueryParam("appname") String appName,
//                               @QueryParam("appversion") String appversion) {
//        AppData appData = new AppData(new MasterData(anbieter, appName, appversion), new MetaData(new DiscoveryObject()));
//
//        return appData;
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/specific/{objname}")
//    public AppData provideInfo(@QueryParam("anbieter") String anbieter,
//                               @QueryParam("appname") String appName,
//                               @QueryParam("appversion") String appversion,
//                               @PathParam("objname") String objName) {
//        //DiscoveryObject dObj = new DiscoveryObject();
//        if (objName.equals("MyDiscoveryObj")) {
//            return new AppData(new MasterData(anbieter, appName, appversion), new MetaData(new DiscoveryObject()));
//        } else {
//            return new AppData(new MasterData(anbieter, appName, appversion), new MetaData(null));
//        }
//    }
//
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response addDiscoveryObject(DiscoveryObject discoveryObject) {
//        discoveryObjectList.add(discoveryObject);
//        return Response.ok().build();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/listall")
//    public List<DiscoveryObject> getDiscoveryObjectList() {
//        return discoveryObjectList;
//    }
//
//}


