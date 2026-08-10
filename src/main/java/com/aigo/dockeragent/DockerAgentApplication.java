package com.aigo.dockeragent;

import com.aigo.dockeragent.rag.PgVectorVectorStoreConfig;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)

public class DockerAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockerAgentApplication.class, args);
	}

}
