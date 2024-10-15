package de.gematik.mdrepo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class DiscoveryObject {
    @Id
    @GeneratedValue(generator = "UUID")
    @Schema(readOnly = true)
    private UUID id;

    private String name;

}
