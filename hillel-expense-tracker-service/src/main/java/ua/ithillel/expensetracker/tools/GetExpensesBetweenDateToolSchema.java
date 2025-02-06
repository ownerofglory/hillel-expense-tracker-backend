package ua.ithillel.expensetracker.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ua.ithillel.expensetracker.client.tool.AgentToolType;

@Data
public class GetExpensesBetweenDateToolSchema implements AgentToolType {
    private String name;
    private String description;

    public GetExpensesBetweenDateToolSchema(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonProperty(value = "type")
    private String type = "object";

    @JsonProperty(value = "properties")
    private GetExpensesBetweenDateToolParams properties = new GetExpensesBetweenDateToolParams();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object getParams() {
        return properties;
    }
}
