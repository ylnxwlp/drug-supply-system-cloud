package com.supply.api.chat;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 *定义接口
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: ChatProto.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ChatServiceGrpc {

  private ChatServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ChatService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.webSocketOnCloseRequest,
      com.google.protobuf.Empty> getWebSocketOnCloseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "webSocketOnClose",
      requestType = com.supply.api.chat.ChatProto.webSocketOnCloseRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.webSocketOnCloseRequest,
      com.google.protobuf.Empty> getWebSocketOnCloseMethod() {
    io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.webSocketOnCloseRequest, com.google.protobuf.Empty> getWebSocketOnCloseMethod;
    if ((getWebSocketOnCloseMethod = ChatServiceGrpc.getWebSocketOnCloseMethod) == null) {
      synchronized (ChatServiceGrpc.class) {
        if ((getWebSocketOnCloseMethod = ChatServiceGrpc.getWebSocketOnCloseMethod) == null) {
          ChatServiceGrpc.getWebSocketOnCloseMethod = getWebSocketOnCloseMethod =
              io.grpc.MethodDescriptor.<com.supply.api.chat.ChatProto.webSocketOnCloseRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "webSocketOnClose"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.chat.ChatProto.webSocketOnCloseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("webSocketOnClose"))
              .build();
        }
      }
    }
    return getWebSocketOnCloseMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.storeChatInformationRequest,
      com.google.protobuf.Empty> getStoreChatInformationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "storeChatInformation",
      requestType = com.supply.api.chat.ChatProto.storeChatInformationRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.storeChatInformationRequest,
      com.google.protobuf.Empty> getStoreChatInformationMethod() {
    io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.storeChatInformationRequest, com.google.protobuf.Empty> getStoreChatInformationMethod;
    if ((getStoreChatInformationMethod = ChatServiceGrpc.getStoreChatInformationMethod) == null) {
      synchronized (ChatServiceGrpc.class) {
        if ((getStoreChatInformationMethod = ChatServiceGrpc.getStoreChatInformationMethod) == null) {
          ChatServiceGrpc.getStoreChatInformationMethod = getStoreChatInformationMethod =
              io.grpc.MethodDescriptor.<com.supply.api.chat.ChatProto.storeChatInformationRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "storeChatInformation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.chat.ChatProto.storeChatInformationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("storeChatInformation"))
              .build();
        }
      }
    }
    return getStoreChatInformationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.sendMessageToUserRequest,
      com.google.protobuf.Empty> getSendMessageToUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendMessageToUser",
      requestType = com.supply.api.chat.ChatProto.sendMessageToUserRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.sendMessageToUserRequest,
      com.google.protobuf.Empty> getSendMessageToUserMethod() {
    io.grpc.MethodDescriptor<com.supply.api.chat.ChatProto.sendMessageToUserRequest, com.google.protobuf.Empty> getSendMessageToUserMethod;
    if ((getSendMessageToUserMethod = ChatServiceGrpc.getSendMessageToUserMethod) == null) {
      synchronized (ChatServiceGrpc.class) {
        if ((getSendMessageToUserMethod = ChatServiceGrpc.getSendMessageToUserMethod) == null) {
          ChatServiceGrpc.getSendMessageToUserMethod = getSendMessageToUserMethod =
              io.grpc.MethodDescriptor.<com.supply.api.chat.ChatProto.sendMessageToUserRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendMessageToUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.chat.ChatProto.sendMessageToUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new ChatServiceMethodDescriptorSupplier("sendMessageToUser"))
              .build();
        }
      }
    }
    return getSendMessageToUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChatServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatServiceStub>() {
        @java.lang.Override
        public ChatServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatServiceStub(channel, callOptions);
        }
      };
    return ChatServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChatServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatServiceBlockingStub>() {
        @java.lang.Override
        public ChatServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatServiceBlockingStub(channel, callOptions);
        }
      };
    return ChatServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChatServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ChatServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ChatServiceFutureStub>() {
        @java.lang.Override
        public ChatServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ChatServiceFutureStub(channel, callOptions);
        }
      };
    return ChatServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   *定义接口
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void webSocketOnClose(com.supply.api.chat.ChatProto.webSocketOnCloseRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWebSocketOnCloseMethod(), responseObserver);
    }

    /**
     */
    default void storeChatInformation(com.supply.api.chat.ChatProto.storeChatInformationRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStoreChatInformationMethod(), responseObserver);
    }

    /**
     */
    default void sendMessageToUser(com.supply.api.chat.ChatProto.sendMessageToUserRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMessageToUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ChatService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static abstract class ChatServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ChatServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ChatService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static final class ChatServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ChatServiceStub> {
    private ChatServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatServiceStub(channel, callOptions);
    }

    /**
     */
    public void webSocketOnClose(com.supply.api.chat.ChatProto.webSocketOnCloseRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWebSocketOnCloseMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void storeChatInformation(com.supply.api.chat.ChatProto.storeChatInformationRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStoreChatInformationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendMessageToUser(com.supply.api.chat.ChatProto.sendMessageToUserRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendMessageToUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ChatService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static final class ChatServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ChatServiceBlockingStub> {
    private ChatServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty webSocketOnClose(com.supply.api.chat.ChatProto.webSocketOnCloseRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWebSocketOnCloseMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty storeChatInformation(com.supply.api.chat.ChatProto.storeChatInformationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStoreChatInformationMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty sendMessageToUser(com.supply.api.chat.ChatProto.sendMessageToUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendMessageToUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ChatService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static final class ChatServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ChatServiceFutureStub> {
    private ChatServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChatServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ChatServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> webSocketOnClose(
        com.supply.api.chat.ChatProto.webSocketOnCloseRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWebSocketOnCloseMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> storeChatInformation(
        com.supply.api.chat.ChatProto.storeChatInformationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStoreChatInformationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> sendMessageToUser(
        com.supply.api.chat.ChatProto.sendMessageToUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendMessageToUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_WEB_SOCKET_ON_CLOSE = 0;
  private static final int METHODID_STORE_CHAT_INFORMATION = 1;
  private static final int METHODID_SEND_MESSAGE_TO_USER = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_WEB_SOCKET_ON_CLOSE:
          serviceImpl.webSocketOnClose((com.supply.api.chat.ChatProto.webSocketOnCloseRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_STORE_CHAT_INFORMATION:
          serviceImpl.storeChatInformation((com.supply.api.chat.ChatProto.storeChatInformationRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_SEND_MESSAGE_TO_USER:
          serviceImpl.sendMessageToUser((com.supply.api.chat.ChatProto.sendMessageToUserRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getWebSocketOnCloseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.chat.ChatProto.webSocketOnCloseRequest,
              com.google.protobuf.Empty>(
                service, METHODID_WEB_SOCKET_ON_CLOSE)))
        .addMethod(
          getStoreChatInformationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.chat.ChatProto.storeChatInformationRequest,
              com.google.protobuf.Empty>(
                service, METHODID_STORE_CHAT_INFORMATION)))
        .addMethod(
          getSendMessageToUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.chat.ChatProto.sendMessageToUserRequest,
              com.google.protobuf.Empty>(
                service, METHODID_SEND_MESSAGE_TO_USER)))
        .build();
  }

  private static abstract class ChatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChatServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.supply.api.chat.ChatProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ChatService");
    }
  }

  private static final class ChatServiceFileDescriptorSupplier
      extends ChatServiceBaseDescriptorSupplier {
    ChatServiceFileDescriptorSupplier() {}
  }

  private static final class ChatServiceMethodDescriptorSupplier
      extends ChatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ChatServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ChatServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChatServiceFileDescriptorSupplier())
              .addMethod(getWebSocketOnCloseMethod())
              .addMethod(getStoreChatInformationMethod())
              .addMethod(getSendMessageToUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
