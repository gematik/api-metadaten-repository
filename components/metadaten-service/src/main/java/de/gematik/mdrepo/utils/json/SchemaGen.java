package de.gematik.mdrepo.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.saasquatch.jsonschemainferrer.AdditionalPropertiesPolicies;
import com.saasquatch.jsonschemainferrer.JsonSchemaInferrer;
import com.saasquatch.jsonschemainferrer.RequiredPolicies;
import com.saasquatch.jsonschemainferrer.SpecVersion;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SchemaGen {

    private final ObjectMapper mapper;
    private final JsonSchemaInferrer inferrer;

    public SchemaGen() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.inferrer = JsonSchemaInferrer.newBuilder()
                .setSpecVersion(SpecVersion.DRAFT_07)
                .setRequiredPolicy(RequiredPolicies.commonFields())
                .setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed())
                .build();
    }

    public void generateAndSaveSchema(String inputFilePath, String outputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);

        if (!inputFile.exists()) {
            throw new IOException("Datei nicht gefunden: " + inputFilePath);
        }

        JsonNode inputJson = mapper.readTree(inputFile);
        JsonNode schemaNode = inferrer.inferForSample(inputJson);
        File outputFile = new File(outputFilePath);

        if (outputFile.getParentFile() != null) {
            outputFile.getParentFile().mkdirs();
        }

        mapper.writeValue(outputFile, schemaNode);
    }

    public static void main(String[] args) {
        SchemaGen generator = new SchemaGen();
        Map<String, String> filesToProcess = new LinkedHashMap<>();

        // Weitere File-Paare koennen der Liste hinzugefuegt werden (k=Inputfile, v=generiertes Schema)
        filesToProcess.put("src/test/resources/example-appdata4.json", "src/test/resources/schema-appdata4.json");

        for (Map.Entry<String, String> entry : filesToProcess.entrySet()) {
            String input = entry.getKey();
            String output = entry.getValue();
            System.out.print("Verarbeite: " + input);

            try {
                generator.generateAndSaveSchema(input, output);
            } catch (Exception e) {
                System.err.println("Fehlernachricht: " + e.getMessage());
            }
        }
    }

}