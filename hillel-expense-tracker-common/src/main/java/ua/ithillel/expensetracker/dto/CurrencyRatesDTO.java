package ua.ithillel.expensetracker.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Data
@JsonDeserialize(using = CurrencyRatesDTO.CurrencyRatesDeserializer.class)
public class CurrencyRatesDTO {

    private Map<String, Double> rates;

    public static class CurrencyRatesDeserializer extends JsonDeserializer<CurrencyRatesDTO> {
        @Override
        public CurrencyRatesDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            CurrencyRatesDTO currencyRatesDTO = new CurrencyRatesDTO();

            Map<String, Double> rates = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (!field.getKey().equals("date")) {
                    JsonNode ratesNode = field.getValue();
                    ratesNode.fields().forEachRemaining(rate ->
                            rates.put(rate.getKey(), rate.getValue().asDouble()));
                }
            }
            currencyRatesDTO.setRates(rates);
            return currencyRatesDTO;
        }
    }
}
