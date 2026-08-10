package com.aigo.dockeragent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PgVectorVectorStoreConfigTest {
    @Resource
    private VectorStore pgVectorVectorStore;
    @Test
    void pgVectorVectorStore() {
        List<Document> documents = List.of(
                new Document("aistudio有什么用？学人工智能啊，做项目啊", Map.of("meta1", "meta1")),
                new Document("aistudio的人工智能项目教程 https://aistudio.baidu.com/"),
                new Document("后端部署比较帅气", Map.of("meta2", "meta2")));
        // 添加文档
        pgVectorVectorStore.add(documents);
        // 相似度查询
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("怎么学人工智能啊").topK(3).build());
        Assertions.assertNotNull(results);
    }
}