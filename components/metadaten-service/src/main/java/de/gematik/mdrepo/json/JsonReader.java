package de.gematik.mdrepo.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import net.jimblackler.jsonschemafriend.*;
import org.apache.commons.io.FileUtils;


public class JsonReader {

    public Pojo readJson2Pojo(String fileLocation) throws IOException {
        File file = new File(fileLocation);
        ObjectMapper objectMapper = new ObjectMapper();
        Pojo pojo = objectMapper.readValue(file, Pojo.class);

        System.out.println("JsonPojo: " + file.toString() + " canRead: " + file.canRead() + " length: " + file.length());
        return pojo;
    }

    public boolean isMatchingSchema(String schemaFileLocation, String jsonFileLocation) throws IOException {
        File schemaFile = new File(schemaFileLocation);
        File jsonFile = new File(jsonFileLocation);
        String schemaStr = FileUtils.readFileToString(schemaFile, Charset.defaultCharset());
        String jsonStr = FileUtils.readFileToString(jsonFile, Charset.defaultCharset());
        try {
            SchemaStore schemaStore = new SchemaStore();
            Schema schema = schemaStore.loadSchemaJson(schemaStr);
            Validator validator = new Validator();
            validator.validateJson(schema, jsonStr);
        } catch (SchemaException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            JsonReader reader = new JsonReader();
            Pojo pojo = reader.readJson2Pojo("src/test/resources/example-appdata4.json");
            System.out.println(pojo.getElements().toString());
            boolean isMatching = reader.isMatchingSchema("src/test/resources/schema-appdata4.json", "src/test/resources/example-appdata4.json");
            System.out.println("validSchemaForThisJson: " + isMatching);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
