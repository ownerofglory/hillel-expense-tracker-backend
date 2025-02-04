package ua.ithillel.expensetracker.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.ithillel.expensetracker.client.tool.GptToolChoice;

@EqualsAndHashCode(callSuper = true)
@Data
public class GptToolResponse extends GptResponse<String> {
    private GptToolChoice tool;
    private boolean intermediate;
}
