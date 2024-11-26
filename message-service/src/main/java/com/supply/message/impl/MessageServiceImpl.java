package com.supply.message.impl;

import com.supply.api.message.MessageProto;
import com.supply.api.message.MessageServiceGrpc;
import com.supply.message.utils.EmailUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {

    private final EmailUtil emailUtil;

    /**
     * 验证码发送
     * @param request 请求信息
     * @param responseObserver 返回信息封装
     */
    public void code(MessageProto.codeRequest request, StreamObserver<MessageProto.codeResponse> responseObserver) {
        String email = request.getEmail();
        emailUtil.codeMail(email);
        MessageProto.codeResponse.Builder builder = MessageProto.codeResponse.newBuilder();
        builder.setCode(emailUtil.codeMail(email).toString());
        MessageProto.codeResponse response = builder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
