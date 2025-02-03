package ua.ithillel.expensetracker.client.tool;

import lombok.Data;

@Data
public class GptTool<T> {
    String name;
    String description;
    T parameters;
}
