package ua.ithillel.expensetracker.client.tool;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GptToolChoice {
    private String toolName;
    private String args;
}
