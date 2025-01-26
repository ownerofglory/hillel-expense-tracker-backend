package ua.ithillel.expensetracker.client.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GptResponseSchema {
    private final GptJsonSchema schema;
    private String name;
    private String description;
    private boolean strict;
}
