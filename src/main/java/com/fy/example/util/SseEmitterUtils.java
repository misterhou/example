package com.fy.example.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

public class SseEmitterUtils {

    private static Map<String, SseEmitter> sseEmitterMap = new HashMap<>();

    public static SseEmitter subscribe(String clientId) {
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitter.onCompletion(() -> {
            System.out.println("连接关闭");
            sseEmitterMap.remove(clientId);
        });
        sseEmitter.onTimeout(() -> {
            System.out.println("连接超时");
            sseEmitterMap.remove(clientId);
        });
        sseEmitter.onError((e) -> {
            System.out.println("连接错误");
            sseEmitterMap.remove(clientId);
        });
        if (sseEmitterMap.containsKey(clientId)) {
            sseEmitterMap.remove(clientId);
        }
        sseEmitterMap.put(clientId, sseEmitter);
        return sseEmitter;
    }

    public static void send(String clientId, String message) {
        SseEmitter sseEmitter = sseEmitterMap.get(clientId);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
