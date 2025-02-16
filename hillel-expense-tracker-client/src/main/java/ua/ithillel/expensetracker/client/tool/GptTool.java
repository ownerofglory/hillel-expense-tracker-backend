package ua.ithillel.expensetracker.client.tool;

import lombok.Data;

@Data
public class GptTool<T extends AgentToolType> {
    String name;
    String description;
    T parameters;
}
