package de.gematik.mdrepo.model;

import lombok.*;
import net.jimblackler.jsonschemafriend.Schema;
import net.jimblackler.jsonschemafriend.SchemaStore;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminData {
    private MasterData masterData;
    //private SchemaStore schemaStore;
    //@Getter @Setter private Schema schema;
    private String schemaStr;
}
