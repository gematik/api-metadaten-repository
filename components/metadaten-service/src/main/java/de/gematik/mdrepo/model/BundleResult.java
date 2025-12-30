package de.gematik.mdrepo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BundleResult {
    public List<String> schemaIds;
    public List<AppData> matchingAppData;

}
