package de.gematik.mdrepo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.gematik.mdrepo.json.JsonReader;
import de.gematik.mdrepo.json.Pojo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Map;

@QuarkusTest
public class JsonReaderTest {

    @Test
    void convertJsonFileToPojoExample1() throws IOException {
        JsonReader reader = new JsonReader();
        Pojo pojo1 = reader.readJson2Pojo("src/test/resources/example1.json");
        System.out.println(pojo1.toString());
        Map map = (Map)pojo1.getElements().get("Services");

        assert(pojo1.getElements().get("Anbieter".toString()).equals("gematik"));
        assert(pojo1.getElements().get("Anwendung".toString()).equals("KIM"));
        assert(String.valueOf(map.get("mailserver_smtp_uri")).equals("smtp://172.17.0.1:8025"));
    }

    @Test
    void jsonFilesMatchSchemas() throws IOException {
        Multimap<String, String> schemaTests = ArrayListMultimap.create();
        schemaTests.put("src/test/resources/schema1.json", "src/test/resources/example1.json");
        schemaTests.put("src/test/resources/schema-appdata1.json", "src/test/resources/example-appdata1.json");
        for (Map.Entry<String, String> entry : schemaTests.entries()) {
            JsonReader reader = new JsonReader();
            boolean isMatching = reader.isMatchingSchema(entry.getKey(), entry.getValue());
            System.out.println("SCHEMATEST: " + entry.getKey() + " MATCHES " + entry.getValue() + ": " + isMatching);
            assert(isMatching);
        }
    }

    @Test
    void jsonFilesFailSchemas() throws IOException {
        Multimap<String, String> schemaTests = ArrayListMultimap.create();
        schemaTests.put("src/test/resources/schema-appdata1.json", "src/test/resources/example1.json");
        for (Map.Entry<String, String> entry : schemaTests.entries()) {
            JsonReader reader = new JsonReader();
            boolean isMatching = reader.isMatchingSchema(entry.getKey(), entry.getValue());
            System.out.println("SCHEMATEST: " + entry.getKey() + " FAILS " + entry.getValue() + ": " + !isMatching);
            assert(!isMatching);
        }
    }

}

