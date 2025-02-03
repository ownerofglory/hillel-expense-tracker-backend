package ua.ithillel.expensetracker.model;

import lombok.Data;

@Data
public class GptMessageContent {
    public static final String CONTENT_TYPE_TEXT = "text";
    public static final String CONTENT_TYPE_IMAGE_URL = "image_url";
    public static final String CONTENT_TYPE_TOOL_CALL = "tool_call";

    private String type;
    private String textContent;
    private String imageUrl;
    private String toolCallResult;
    private String toolCallId;
}
