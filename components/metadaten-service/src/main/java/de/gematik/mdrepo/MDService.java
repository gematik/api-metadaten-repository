package de.gematik.mdrepo;

import de.gematik.mdrepo.model.AdminData;
import de.gematik.mdrepo.model.AppData;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Data
public class MDService {

    private List<AdminData> adminDataList = new ArrayList<>();
    private List<AppData> appDataList = new ArrayList<>();
}
