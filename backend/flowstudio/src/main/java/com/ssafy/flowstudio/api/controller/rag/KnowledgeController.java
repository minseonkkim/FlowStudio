package com.ssafy.flowstudio.api.controller.rag;

import com.drew.lang.annotations.NotNull;
import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeCreateRequest;
import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeRequest;
import com.ssafy.flowstudio.api.controller.rag.request.KnowledgeSearchRequest;
import com.ssafy.flowstudio.api.service.rag.KnowledgeService;
import com.ssafy.flowstudio.api.service.rag.VectorStoreService;
import com.ssafy.flowstudio.api.service.rag.request.KnowledgeSearchServiceRequest;
import com.ssafy.flowstudio.api.service.rag.response.*;
import com.ssafy.flowstudio.common.annotation.CurrentUser;
import com.ssafy.flowstudio.common.payload.ApiResponse;
import com.ssafy.flowstudio.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/knowledges")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;
    private final VectorStoreService vectorStoreService;

    /**
     * 문서 목록 조회
     *
     * @param user
     * @return List<KnowledgeListResponse>
     */
    @GetMapping
    public ApiResponse<List<KnowledgeListResponse>> getKnowledges(
            @CurrentUser User user,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {

        return ApiResponse.ok(knowledgeService.getKnowledges(user, page, limit));
    }

    /**
     * 문서 등록
     *
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
     * 문서 수정
     *
     * @param user
     * @param knowledgeId
     * @param request
     * @return KnowledgeResponse
     */
    @PutMapping("{knowledgeId}")
    public ApiResponse<KnowledgeResponse> updateKnowledge(
            @CurrentUser User user,
            @PathVariable Long knowledgeId,
            @RequestBody KnowledgeRequest request
    ) {
        return ApiResponse.ok(knowledgeService.updateKnowledge(user, knowledgeId, request.toServiceRequest()));
    }

    /**
     * 문서 삭제
     *
     * @param user
     * @param knowledgeId
     * @return Boolean
     */
    @DeleteMapping("{knowledgeId}")
    public ApiResponse<Boolean> deleteKnowledge(
            @CurrentUser User user,
            @PathVariable Long knowledgeId
    ) {
        return ApiResponse.ok(knowledgeService.deleteKnowledge(user, knowledgeId));
    }

    /**
     * 지식 복제하기
     *
     * @param user
     * @param knowledgeId
     * @return KnowledgeResponse
     */
    @PostMapping("{knowledgeId}/copy")
    public ApiResponse<KnowledgeResponse> copyKnowledge(
            @CurrentUser User user,
            @PathVariable Long knowledgeId
    ) {
        return ApiResponse.ok(vectorStoreService.copyDocument(user, knowledgeId));
    }

    /**
     * 청크 미리보기
     *
     * @param user
     * @param request
     * @return List<String>
     */
    @PostMapping("chunks")
    public ApiResponse<ChunkListResponse> knowledgeChunks(
            @CurrentUser User user,
            @Valid @ModelAttribute KnowledgeCreateRequest request
    ) {
        return ApiResponse.ok(vectorStoreService.previewChunks(request.toServiceRequest()));
    }

    /**
     * 청크 목록 보기
     *
     * @param user
     * @param knowledgeId
     * @return ChunkListResponse
     */
    @GetMapping("{knowledgeId}/chunks")
    public ApiResponse<ChunkListResponse> knowledgeDetails(
            @CurrentUser User user,
            @PathVariable Long knowledgeId
    ) {
        return ApiResponse.ok(vectorStoreService.getDocumentChunks(user, knowledgeService.getKnowledge(user, knowledgeId), -1L));
    }

    /**
     * 청크 상세 보기
     *
     * @param user
     * @param knowledgeId
     * @param chunkId
     * @return List<ChunkResponse>
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
     *
     * @param user
     * @param knowledgeId
     * @param chunkId
     * @param request
     * @return Boolean
     */
    @PostMapping("{knowledgeId}/chunks/{chunkId}")
    public ApiResponse<Boolean> knowledgeChunkUpdate(
            @CurrentUser User user,
            @PathVariable Long knowledgeId,
            @PathVariable Long chunkId,
            @NotNull @RequestBody Map<String, String> request
    ) {
        return ApiResponse.ok(vectorStoreService.upsertChunk(user, knowledgeService.getKnowledge(user, knowledgeId), chunkId, request.get("content")));
    }

    /**
     * 청크 삭제
     *
     * @param user
     * @param knowledgeId
     * @param chunkId
     * @return Boolean
     */
    @DeleteMapping("{knowledgeId}/chunks/{chunkId}")
    public ApiResponse<Boolean> knowledgeChunkDelete(
            @CurrentUser User user,
            @PathVariable Long knowledgeId,
            @PathVariable Long chunkId
    ) {
        return ApiResponse.ok(vectorStoreService.deleteChunk(user, knowledgeService.getKnowledge(user, knowledgeId), chunkId));
    }

    /**
     * 벡터 검색
     *
     * @param request
     * @return List<String>
     */
    @GetMapping("search")
    public ApiResponse<List<String>> search(
            @NotNull @Valid @RequestBody KnowledgeSearchRequest request
    ) {
        KnowledgeSearchResponse knowledgeSearchResponse = knowledgeService.getKnowledge(request.getKnowledgeId());

        return ApiResponse.ok(vectorStoreService.searchVector(KnowledgeSearchServiceRequest.from(request, knowledgeSearchResponse)));
    }
}
