package com.aigo.dockeragent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DockerAppTest {
    @Resource
    private DockerApp dockerApp;
    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员go";
        String answer = dockerApp.doChat(message, chatId);
        // 第二轮
        message = "轻微流鼻涕";
        answer = dockerApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "刚跟你说过，帮我回忆一下";
        answer = dockerApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员go，我感冒了想吃药，但我不知道该怎么做";
        DockerApp.DockersReport dockersReport = dockerApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(dockersReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我是程序员go，肠胃有些问题，不知道看哪个书怎么办？";
        String answer = dockerApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

}