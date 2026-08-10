package com.aigo.dockeragent.app;

import com.aigo.dockeragent.advisor.MyLoggerAdvisor;
import com.aigo.dockeragent.advisor.ReReadingAdvisor;
import com.aigo.dockeragent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;



import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class DockerApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕医学领域的资深专家。开场向用户表明身份，告知用户可倾诉的健康难题" +
            "围绕抑郁症、肠胃病、麻痹症三种病症提问：肠胃病询问干呕，不想吃饭，反胃，反酸水，发烧；麻痹症身体不能动，手脚发麻；抑郁症，情绪低落等" +
            "围绕用户健康状态分场景引导提问：询问症状具体表现、持续时间、是否有其他伴随不适等。" +
            "引导用户详述病情经过、自身感受及相关诉求，以便结合专业医疗知识给出专属解决方案。";

    public DockerApp(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
        //ChatMemory chatMemory = new InMemoryChatMemory();
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                        //new ReReadingAdvisor()
                )
                .build();
    }

    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


    record DockersReport(String title, List<String> suggestions) {

    }

    /**
     * ai病理报告输出（结构化输出）
     *
     * @param message
     * @param chatId
     * @return
     */
    public DockersReport doChatWithReport(String message, String chatId) {
        DockersReport dockersReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成病理结果，标题为{用户名}的病理报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(DockersReport.class);
        log.info("loveReport: {}", dockersReport);
        return dockersReport;
    }

    @Resource
    private VectorStore docAppVectorStore;
    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答
                .advisors(new QuestionAnswerAdvisor(docAppVectorStore))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }


}



