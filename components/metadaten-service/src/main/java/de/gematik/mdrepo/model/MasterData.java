package de.gematik.mdrepo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MasterData {
    private String anbieter;
    private String appName;
    private String appVersion;
}
