package ua.ithillel.expensetracker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ua.ithillel.expensetracker.model.GptMessage;
import ua.ithillel.expensetracker.model.GptToolResponse;
import ua.ithillel.expensetracker.service.SmartAgentService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/agent")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class SmartAgentController {
    private final SmartAgentService smartAgentService;

    @Autowired
    @Qualifier("executor")
    private ExecutorService executor;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getChatCompletion(@RequestParam("userId") Long userId,
                                        @RequestBody List<GptMessage> messages) {
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                Stream<GptToolResponse> chatCompletionWithTools = smartAgentService.getChatCompletionWithTools(messages, userId);

                chatCompletionWithTools.forEach(resp -> {
                    try {
                        String content = resp.getContent();
                        if (content == null) {
                            emitter.complete();
                            return;
                        }

                        String dataContent = content.replaceAll("\n", "<br/>");
                        SseEmitter.SseEventBuilder event = SseEmitter.event()
                                .id(String.valueOf(System.currentTimeMillis()))
                                .name("message")
                                .data(dataContent);
                        Thread.sleep(20);
                        emitter.send(event);

                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    } catch (InterruptedException e) {
                        emitter.completeWithError(e);
                    }
                });
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
