package ua.ithillel.expensetracker.client.model;

import lombok.Data;

@Data
public class GptMessageContent {
    private String type;
    private String textContent;
    private String imageUrl;
}
