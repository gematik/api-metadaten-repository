package de.gematik.mdrepo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class MasterData {

    @Id
    @GeneratedValue(generator = "UUID")
    @Schema(readOnly = true)
    private UUID id;

    private String anbieter;
    private String appName;
    private String appVersion;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DiscoveryObject> discoveryObjects;
}
