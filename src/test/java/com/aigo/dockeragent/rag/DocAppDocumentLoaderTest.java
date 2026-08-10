package com.aigo.dockeragent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DocAppDocumentLoaderTest {
    @Resource
    private DocAppDocumentLoader docAppDocumentLoader;
    @Test
    void loadMarkdowns() {
        docAppDocumentLoader.loadMarkdowns();
    }
}