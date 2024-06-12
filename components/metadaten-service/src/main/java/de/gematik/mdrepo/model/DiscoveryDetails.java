package de.gematik.mdrepo.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;


@NoArgsConstructor
@ToString
@Data
public class DiscoveryDetails {

    private Map<String, Object> elements = new LinkedHashMap<String, Object>();
}
