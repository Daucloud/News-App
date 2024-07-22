package com.java.zhangxinyuan.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Assistant {
    static public String getEndDate() {
        // 创建日期对象并获取当前时间
        Date currentDate = new Date();
        // 创建日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 格式化日期对象为字符串
        return dateFormat.format(currentDate);
    }

    static public String getSummary(String content) {
        final String API_KEY = "4396d92d403d36d546790c1d9da8d6cc.kCxpZSgF5tnxs3Qq";
        final ClientV4 client = new ClientV4.Builder(API_KEY).build();
        List<ChatMessage> messages = List.of(new ChatMessage[]{
                new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "你的任务是为用户给定的新闻文本生成一篇总结摘要。注意，除了摘要的正文，不需要添加任何额外内容。")
                , new ChatMessage(ChatMessageRole.USER.value(),
                content)});

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
    }
}
