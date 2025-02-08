package ua.ithillel.expensetracker.service;

import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptToolResponse;

import java.util.List;
import java.util.stream.Stream;

public interface SmartAgentService {
    Stream<GptToolResponse> getChatCompletionWithTools(List<GptMessage> messages, Long userId);
}
