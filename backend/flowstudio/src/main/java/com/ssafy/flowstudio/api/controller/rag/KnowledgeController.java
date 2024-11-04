package com.ssafy.flowstudio.api.controller.rag;

import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeCreateRequest;
import com.ssafy.flowstudio.api.service.rag.KnowledgeService;
import com.ssafy.flowstudio.api.service.rag.VectorStoreService;
import com.ssafy.flowstudio.api.service.rag.response.ChunkListResponse;
import com.ssafy.flowstudio.api.service.rag.response.ChunkResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeListResponse;
import com.ssafy.flowstudio.api.service.rag.response.KnowledgeResponse;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.knowledge.entity.KnowledgeRepository;
import com.ssafy.flowstudio.domain.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/knowledges")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;
    private final VectorStoreService vectorStoreService;

    /**
     * 문서 목록 조회
     * @param user
     * @return List<KnowledgeListResponse>
     */
    @GetMapping
    public ApiResponse<List<KnowledgeListResponse>> getKnowledges(@CurrentUser User user) {

        return ApiResponse.ok(knowledgeService.getKnowledges(user));
    }

    /**
     * 문서 등록
     * @param user
     * @param request
     * @return KnowledgeResponse
     */
    @PostMapping
    public ApiResponse<KnowledgeResponse> createKnowledge(
            @CurrentUser User user,
            @Valid @ModelAttribute KnowledgeCreateRequest request
    ) {
        return ApiResponse.ok(knowledgeService.createKnowledge(user, request.toServiceRequest()));
    }

    /**
     * 청크 미리보기
     * @param user
     * @param request
     * @return
     */
    @GetMapping("chunks")
    public ApiResponse<List<String>> knowledgeChunks(
            @CurrentUser User user,
            @Valid @ModelAttribute KnowledgeCreateRequest request
    ) {
        return ApiResponse.ok(vectorStoreService.previewChunks(request.toServiceRequest()));
    }

    /**
     * 청크 목록 보기
     * @param user
     * @param knowledgeId
     * @return
     */
    @GetMapping("{knowledgeId}")
    public ApiResponse<ChunkListResponse> knowledgeDetails(
            @CurrentUser User user,
            @PathVariable Long knowledgeId
    ) {
        return ApiResponse.ok(vectorStoreService.getDocumentChunks(user, knowledgeService.getKnowledge(user, knowledgeId), -1L));
    }

    /**
     * 청크 상세 보기
     * @param user
     * @param knowledgeId
     * @param chunkId
     * @return
     */
    @GetMapping("{knowledgeId}/chunks/{chunkId}")
    public ApiResponse<List<ChunkResponse>> knowledgeChunkDetails(
            @CurrentUser User user,
            @PathVariable Long knowledgeId,
            @PathVariable Long chunkId
    ) {
        return ApiResponse.ok(vectorStoreService.getDocumentChunk(user, knowledgeService.getKnowledge(user, knowledgeId), chunkId));
    }

    /**
     * 청크 등록/수정
     * @param user
     * @param knowledgeId
     * @param chunkId
     * @param content
     * @return
     */
    @PostMapping("{knowledgeId}/chunks/{chunkId}")
    public ApiResponse knowledgeChunkUpdate(
            @CurrentUser User user,
            @PathVariable Long knowledgeId,
            @PathVariable Long chunkId,
            String content
    ) {
        KnowledgeResponse knowledgeResponse = knowledgeService.getKnowledge(user, knowledgeId);
        vectorStoreService.upsertChunk(user, knowledgeResponse, chunkId, content);
        return ApiResponse.ok();
    }

    /**
     * 청크 삭제
     * @param user
     * @param knowledgeId
     * @param chunkId
     * @return
     */
    @DeleteMapping("{knowledgeId}/chunks/{chunkId}")
    public ApiResponse knowledgeChunkDelete(
            @CurrentUser User user,
            @PathVariable Long knowledgeId,
            @PathVariable Long chunkId
    ) {
        vectorStoreService.deleteChunk(user, knowledgeService.getKnowledge(user, knowledgeId), chunkId);
        return ApiResponse.ok();
    }

    @GetMapping("{knowledgeId}/search")
    public String search(@CurrentUser User user, @PathVariable Long knowledgeId, @RequestParam String query) {
        vectorStoreService.search(user, knowledgeService.getKnowledge(user, knowledgeId), query);

        return "search";
    }
}
