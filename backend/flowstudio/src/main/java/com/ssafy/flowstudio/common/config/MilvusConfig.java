package com.ssafy.flowstudio.common.config;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {
    @Value("${milvus.host}")
    private String milvusHost;

    @Value("${milvus.port}")
    private String milvusPort;

    @Value("${milvus.token}")
    private String milvusToken;

    @Value("${milvus.db.name}")
    private String milvusDbName;


    @Bean
    public MilvusClientV2 milvusClient() {
        ConnectConfig connectParam = ConnectConfig.builder()
                .uri(String.join(":",milvusHost, milvusPort))
                .dbName(milvusDbName)
                .token(milvusToken)
                .build();

        return new MilvusClientV2(connectParam);
    }
}
