package com.fy.example.sse;

import com.fy.example.util.SseEmitterUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/sse")
public class NoticeController {

    @RequestMapping("/notice")
    public SseEmitter createSseEmitter(String clientId) {
        CompletableFuture.runAsync(() -> {
            while (true) {
                SseEmitterUtils.send(clientId, "hello");
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return SseEmitterUtils.subscribe(clientId);
    }
}
