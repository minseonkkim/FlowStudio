package com.ssafy.flowstudio.api.service.rag.request;

import com.ssafy.flowstudio.api.service.rag.response.TextSplitServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="langchain", url="${langchain.fastapi.url}")
public interface LangchainClient {

    @PostMapping("api/v1/langchain/split_text")
    TextSplitServiceResponse getSplitText(@RequestBody TextSplitServiceRequest request);

}