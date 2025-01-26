package ua.ithillel.expensetracker.client.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GptJsonSchema {

    @JsonProperty("type")
    private final String type = "object";

    @JsonProperty("additionalProperties")
    private final boolean additionalProperties;

    @JsonProperty("required")
    private final List<String> required;

    @JsonProperty("properties")
    private final Map<String, GptJsonSchema.PropertyDefinition> properties;

    public GptJsonSchema(ObjectSchema schema) {
        this.additionalProperties = false;
        this.required = new ArrayList<>();
        this.properties = new HashMap<>();

        schema.getProperties().forEach((propertyName, propertySchema) -> {
            properties.put(propertyName, new GptJsonSchema.PropertyDefinition(propertySchema));

            if (propertySchema.getRequired()) {
                required.add(propertyName);
            }
        });
    }

    @Data
    public static class PropertyDefinition {

        @JsonProperty("type")
        private final String type;

        @JsonProperty("description")
        private final String description;

        public PropertyDefinition(JsonSchema propertySchema) {
            String typeStr = "";
            JsonFormatTypes schemaType = propertySchema.getType();
            typeStr = schemaType.value();
            if (schemaType == JsonFormatTypes.INTEGER || schemaType == JsonFormatTypes.NUMBER) {
                typeStr = "number";
            }
            this.type = typeStr;
            this.description = propertySchema.getDescription();
        }
    }
}
