package de.gematik.mdrepo.model;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiscoveryObject {
    //private ConfigurationParameter configurationParameter;
    //private Policy policy;
    //private Service service;
//    private String name = "MyDiscoveryObj";
    @Getter @Setter private String name;
//    private UUID uuid = new UUID(128, 16);
    @Getter @Setter private UUID uuid;
//    @Getter @Setter private String name;
    //private UUID uuid;
//    private DiscoveryDetails discoveryDetails = new DiscoveryDetails();
//    @Getter @Setter private DiscoveryDetails discoveryDetails;
    private Map<String, Object> discoveryDetails = new LinkedHashMap<String, Object>();

    //private DiscoveryRepresentation discoveryRepresentation;

}
