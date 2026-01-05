package de.gematik.mdrepo.utils.json;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@ToString
//@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class Pojo {

    @Getter private Map<String, Object> elements = new LinkedHashMap<String, Object>();

    @JsonAnySetter
    public void add(String key, Object value) {
        elements.put(key, value);
    }

}
