package com.ssafy.flowstudio.docs.rag;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ssafy.flowstudio.api.controller.rag.KnowledgeController;
import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeCreateRequest;
import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeRequest;
import com.ssafy.flowstudio.api.service.node.request.NodeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.KnowledgeService;
import com.ssafy.flowstudio.api.service.rag.VectorStoreService;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeCreateServiceRequest;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.ChunkListResponse;
import com.ssafy.flowstudio.api.service.rag.response.ChunkResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeListResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.docs.RestDocsSupport;
import com.ssafy.flowstudio.domain.knowledge.entity.Knowledge;
import com.ssafy.flowstudio.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;


public class KnowledgeControllerDocsTest extends RestDocsSupport {

    private final KnowledgeService knowledgeService = mock(KnowledgeService.class);
    private final VectorStoreService vectorStoreService = mock(VectorStoreService.class);

    @Override
    protected Object initController() {
        return new KnowledgeController(knowledgeService, vectorStoreService);
    }

    @DisplayName("지식베이스 목록")
    @Test
    void getKnowledge() throws Exception {
        // given
        List<KnowledgeListResponse> response = List.of(
                KnowledgeListResponse.builder()
                        .knowledgeId(1L)
                        .title("test.txt")
                        .isPublic(true)
                        .build()
        );

        given(knowledgeService.getKnowledges(any(User.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/knowledges")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("get-knowledges",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 목록 가져오기")
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data[].knowledgeId").type(JsonFieldType.NUMBER).description("지식베이스 아이디"),
                                        fieldWithPath("data[].title").type(JsonFieldType.STRING).description("문서(파일명)"),
                                        fieldWithPath("data[].isPublic").type(JsonFieldType.BOOLEAN).description("공유여부")
                                )
                                .build()
                        )
                ));

    }

    @DisplayName("지식베이스 수정")
    @Test
    void updateKnowledge() throws Exception {
        // given
        KnowledgeRequest request = KnowledgeRequest.builder()
                .title("update.txt")
                .isPublic(true)
                .build();

        KnowledgeResponse response = KnowledgeResponse.builder()
                .knowledgeId(1L)
                .title("update.txt")
                .isPublic(true)
                .createAt(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        given(knowledgeService.updateKnowledge(any(User.class), any(Long.class), any(KnowledgeServiceRequest.class)))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                put("/api/v1/knowledges/{knowledgeId}", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("update-knowledge",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 수정")
                                .pathParameters(
                                        parameterWithName("knowledgeId").description("지식베이스 아이디")
                                )
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("문서(파일명)"),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("공유여부")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data.knowledgeId").type(JsonFieldType.NUMBER).description("지식베이스 아이디"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("문서(파일명)"),
                                        fieldWithPath("data.isPublic").type(JsonFieldType.BOOLEAN).description("공유여부"),
                                        fieldWithPath("data.createAt").type(JsonFieldType.STRING).description("등록일")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("지식베이스 삭제")
    @Test
    void deleteKnowledge() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/knowledges/{knowledgeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("delete-knowledge",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 삭제")
                                .pathParameters(
                                        parameterWithName("knowledgeId").description("지식베이스 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("성공여부")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("지식베이스 청크 목록")
    @Test
    void getKnowledgeChunks() throws Exception {
        // given
        ChunkListResponse response = ChunkListResponse.builder()
                .chunkList(
                        List.of(
                                ChunkResponse.builder().chunkId(0).content("sentence1").build()
                        )
                )
                .chunkCount(1)
                .build();

        given(vectorStoreService.getDocumentChunks(any(), any(), any()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/knowledges/{knowledgeId}/chunks", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("get-knowledge-chunks",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 청크 목록")
                                .pathParameters(
                                        parameterWithName("knowledgeId").description("지식베이스 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data.chunkCount").type(JsonFieldType.NUMBER).optional().description("청크개수"),
                                        fieldWithPath("data.chunkList[].chunkId").type(JsonFieldType.NUMBER).optional().description("청크 아이디"),
                                        fieldWithPath("data.chunkList[].content").type(JsonFieldType.STRING).optional().description("청크 내용")
                                )
                                .build()
                        )
                ));
    }

    @DisplayName("지식베이스 청크 상세보기")
    @Test
    void getKnowledgeChunkDetail() throws Exception {
        // given
        List<ChunkResponse> response = List.of(
                ChunkResponse.builder().chunkId(0).content("sentence1").build()
        );

        given(vectorStoreService.getDocumentChunk(any(), any(), any()))
                .willReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                get("/api/v1/knowledges/{knowledgeId}/chunks/{chunkId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("get-knowledge-chunk-detail",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 청크 상세보기")
                                .pathParameters(
                                        parameterWithName("knowledgeId").description("지식베이스 아이디"),
                                        parameterWithName("chunkId").description("청크 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data[].chunkId").type(JsonFieldType.NUMBER).optional().description("청크 아이디"),
                                        fieldWithPath("data[].content").type(JsonFieldType.STRING).optional().description("청크 내용")
                                )
                                .build()
                        )
                ));

    }

    @DisplayName("지식베이스 청크 수정")
    @Test
    void updateKnowledgeChunk() throws Exception {
        // given

        given(vectorStoreService.upsertChunk(any(User.class), any(KnowledgeResponse.class), any(Long.class), any(String.class)))
                .willReturn(Boolean.TRUE);

        // when
        ResultActions perform = mockMvc.perform(
                post("/api/v1/knowledges/{knowledgeId}/chunks/{chunkId}", 1L, 1L)
                        .content(objectMapper.writeValueAsString(Map.of("content", "content내용")))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("update-knowledge-chunk-detail",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 청크 수정")
                                .pathParameters(
                                        parameterWithName("knowledgeId").description("지식베이스 아이디"),
                                        parameterWithName("chunkId").description("청크 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("성공여부")
                                )
                                .build()
                        )
                ));

    }

    @DisplayName("지식베이스 청크 삭제")
    @Test
    void deleteKnowledgeChunk() throws Exception {
        // given
        given(vectorStoreService.upsertChunk(any(User.class), any(KnowledgeResponse.class), any(Long.class), any(String.class)))
                .willReturn(Boolean.TRUE);

        // when
        ResultActions perform = mockMvc.perform(
                delete("/api/v1/knowledges/{knowledgeId}/chunks/{chunkId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("delete-knowledge-chunk-detail",
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Knowledge")
                                .summary("지식베이스 청크 삭제")
                                .pathParameters(
                                        parameterWithName("knowledgeId").description("지식베이스 아이디"),
                                        parameterWithName("chunkId").description("청크 아이디")
                                )
                                .responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("성공여부")
                                )
                                .build()
                        )
                ));

    }


}
