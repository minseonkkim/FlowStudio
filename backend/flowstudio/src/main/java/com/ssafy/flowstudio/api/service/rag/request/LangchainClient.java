package com.ssafy.flowstudio.api.service.rag.request;

import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.request.ChatFlowTestServiceRequest;
import com.ssafy.flowstudio.api.service.chatflowtest.response.ChatFlowTestResponse;
import com.ssafy.flowstudio.api.service.rag.response.TextSplitServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="langchain", url="${langchain.fastapi.url}")
public interface LangchainClient {

    @PostMapping("langchain/split_text")
    TextSplitServiceResponse getSplitText(@RequestBody TextSplitServiceRequest request);

    @PostMapping("langchain/evaluation")
    ChatFlowTestResponse chatFlowTest(@RequestBody ChatFlowTestRequest request);
}
