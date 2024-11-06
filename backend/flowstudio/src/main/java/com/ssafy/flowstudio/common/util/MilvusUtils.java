package com.ssafy.flowstudio.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import dev.langchain4j.data.message.ChatMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class MilvusUtils {
    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    public String generateName(Long id) {
        return String.join("_", "", id.toString());
    }

    public List<String> generateName(List<Long> ids) {
        return ids.stream()
                .map(this::generateName)
                .toList();
    }

    public EmbeddingModel generateEmbeddingModel() {
        return new OpenAiEmbeddingModel(
                new OpenAiApi(openAiApiKey),
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .withModel("text-embedding-3-large")
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE
        );
    }

    public String getTextContent(MultipartFile file) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(file.getResource());

        List<Document> documents = tikaDocumentReader.get();

        StringBuilder sb = new StringBuilder();
        for (Document document : documents) {
            sb.append(document.getContent()).append("\n");
        }

        return sb.toString();
    }

    public JsonObject documentToJson(Long chunkId, String content) {
        EmbeddingModel embeddingModel = generateEmbeddingModel();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", chunkId);
        JsonArray vectors = new JsonArray();
        for (float vector : embeddingModel.embed(content)) {
            vectors.add(vector);
        }
        jsonObject.add("vector", vectors);
        jsonObject.addProperty("content", content);

        return jsonObject;
    }

    public List<Document> textsToDocuments(List<String> splitText) {
        return splitText.stream()
                .map(chunk -> Document.builder()
                        .withContent(chunk)
                        .build())
                .toList();
    }

    public int getTokenCount(List<String> messages) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        ModelType model = ModelType.TEXT_EMBEDDING_3_LARGE;
        Encoding encoding = registry.getEncodingForModel(model);
        int sum = 0;
        for (String message : messages) {
            sum += encoding.countTokens(message);
        }

        return sum;
    }
}
