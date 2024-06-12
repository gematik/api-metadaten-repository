package de.gematik.mdrepo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MetaData {
    private DiscoveryObject discoveryObject;
    private static final List<DiscoveryObject> discoveryObjectList = new ArrayList<>();

}
