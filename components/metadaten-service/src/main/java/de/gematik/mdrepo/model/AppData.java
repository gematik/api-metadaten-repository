package de.gematik.mdrepo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppData {
    private MasterData masterData;
    private MetaData metaData;

}
