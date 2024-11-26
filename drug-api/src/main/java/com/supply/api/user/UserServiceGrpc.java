package com.supply.api.user;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 *定义接口
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: UserProto.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authenticationRequest,
      com.supply.api.user.UserProto.authenticationResponse> getGetUserInformationByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getUserInformationById",
      requestType = com.supply.api.user.UserProto.authenticationRequest.class,
      responseType = com.supply.api.user.UserProto.authenticationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authenticationRequest,
      com.supply.api.user.UserProto.authenticationResponse> getGetUserInformationByIdMethod() {
    io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authenticationRequest, com.supply.api.user.UserProto.authenticationResponse> getGetUserInformationByIdMethod;
    if ((getGetUserInformationByIdMethod = UserServiceGrpc.getGetUserInformationByIdMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetUserInformationByIdMethod = UserServiceGrpc.getGetUserInformationByIdMethod) == null) {
          UserServiceGrpc.getGetUserInformationByIdMethod = getGetUserInformationByIdMethod =
              io.grpc.MethodDescriptor.<com.supply.api.user.UserProto.authenticationRequest, com.supply.api.user.UserProto.authenticationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getUserInformationById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.user.UserProto.authenticationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.user.UserProto.authenticationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("getUserInformationById"))
              .build();
        }
      }
    }
    return getGetUserInformationByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authorityRequest,
      com.supply.api.user.UserProto.authorityResponse> getGetUserAuthorityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getUserAuthority",
      requestType = com.supply.api.user.UserProto.authorityRequest.class,
      responseType = com.supply.api.user.UserProto.authorityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authorityRequest,
      com.supply.api.user.UserProto.authorityResponse> getGetUserAuthorityMethod() {
    io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authorityRequest, com.supply.api.user.UserProto.authorityResponse> getGetUserAuthorityMethod;
    if ((getGetUserAuthorityMethod = UserServiceGrpc.getGetUserAuthorityMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetUserAuthorityMethod = UserServiceGrpc.getGetUserAuthorityMethod) == null) {
          UserServiceGrpc.getGetUserAuthorityMethod = getGetUserAuthorityMethod =
              io.grpc.MethodDescriptor.<com.supply.api.user.UserProto.authorityRequest, com.supply.api.user.UserProto.authorityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getUserAuthority"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.user.UserProto.authorityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.user.UserProto.authorityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("getUserAuthority"))
              .build();
        }
      }
    }
    return getGetUserAuthorityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authorityRequest,
      com.supply.api.user.UserProto.authenticationResponse> getGetUserInformationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getUserInformation",
      requestType = com.supply.api.user.UserProto.authorityRequest.class,
      responseType = com.supply.api.user.UserProto.authenticationResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authorityRequest,
      com.supply.api.user.UserProto.authenticationResponse> getGetUserInformationMethod() {
    io.grpc.MethodDescriptor<com.supply.api.user.UserProto.authorityRequest, com.supply.api.user.UserProto.authenticationResponse> getGetUserInformationMethod;
    if ((getGetUserInformationMethod = UserServiceGrpc.getGetUserInformationMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetUserInformationMethod = UserServiceGrpc.getGetUserInformationMethod) == null) {
          UserServiceGrpc.getGetUserInformationMethod = getGetUserInformationMethod =
              io.grpc.MethodDescriptor.<com.supply.api.user.UserProto.authorityRequest, com.supply.api.user.UserProto.authenticationResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getUserInformation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.user.UserProto.authorityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.user.UserProto.authenticationResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("getUserInformation"))
              .build();
        }
      }
    }
    return getGetUserInformationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceStub>() {
        @java.lang.Override
        public UserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceStub(channel, callOptions);
        }
      };
    return UserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub>() {
        @java.lang.Override
        public UserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingStub(channel, callOptions);
        }
      };
    return UserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub>() {
        @java.lang.Override
        public UserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceFutureStub(channel, callOptions);
        }
      };
    return UserServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   *定义接口
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void getUserInformationById(com.supply.api.user.UserProto.authenticationRequest request,
        io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authenticationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserInformationByIdMethod(), responseObserver);
    }

    /**
     */
    default void getUserAuthority(com.supply.api.user.UserProto.authorityRequest request,
        io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authorityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserAuthorityMethod(), responseObserver);
    }

    /**
     */
    default void getUserInformation(com.supply.api.user.UserProto.authorityRequest request,
        io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authenticationResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserInformationMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UserService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static abstract class UserServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UserServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UserService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static final class UserServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UserServiceStub> {
    private UserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     */
    public void getUserInformationById(com.supply.api.user.UserProto.authenticationRequest request,
        io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authenticationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserInformationByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUserAuthority(com.supply.api.user.UserProto.authorityRequest request,
        io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authorityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserAuthorityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUserInformation(com.supply.api.user.UserProto.authorityRequest request,
        io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authenticationResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserInformationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UserService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static final class UserServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.supply.api.user.UserProto.authenticationResponse getUserInformationById(com.supply.api.user.UserProto.authenticationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserInformationByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.supply.api.user.UserProto.authorityResponse getUserAuthority(com.supply.api.user.UserProto.authorityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserAuthorityMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.supply.api.user.UserProto.authenticationResponse getUserInformation(com.supply.api.user.UserProto.authorityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserInformationMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UserService.
   * <pre>
   *定义接口
   * </pre>
   */
  public static final class UserServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UserServiceFutureStub> {
    private UserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.supply.api.user.UserProto.authenticationResponse> getUserInformationById(
        com.supply.api.user.UserProto.authenticationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserInformationByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.supply.api.user.UserProto.authorityResponse> getUserAuthority(
        com.supply.api.user.UserProto.authorityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserAuthorityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.supply.api.user.UserProto.authenticationResponse> getUserInformation(
        com.supply.api.user.UserProto.authorityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserInformationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_USER_INFORMATION_BY_ID = 0;
  private static final int METHODID_GET_USER_AUTHORITY = 1;
  private static final int METHODID_GET_USER_INFORMATION = 2;

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
        case METHODID_GET_USER_INFORMATION_BY_ID:
          serviceImpl.getUserInformationById((com.supply.api.user.UserProto.authenticationRequest) request,
              (io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authenticationResponse>) responseObserver);
          break;
        case METHODID_GET_USER_AUTHORITY:
          serviceImpl.getUserAuthority((com.supply.api.user.UserProto.authorityRequest) request,
              (io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authorityResponse>) responseObserver);
          break;
        case METHODID_GET_USER_INFORMATION:
          serviceImpl.getUserInformation((com.supply.api.user.UserProto.authorityRequest) request,
              (io.grpc.stub.StreamObserver<com.supply.api.user.UserProto.authenticationResponse>) responseObserver);
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
          getGetUserInformationByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.user.UserProto.authenticationRequest,
              com.supply.api.user.UserProto.authenticationResponse>(
                service, METHODID_GET_USER_INFORMATION_BY_ID)))
        .addMethod(
          getGetUserAuthorityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.user.UserProto.authorityRequest,
              com.supply.api.user.UserProto.authorityResponse>(
                service, METHODID_GET_USER_AUTHORITY)))
        .addMethod(
          getGetUserInformationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.user.UserProto.authorityRequest,
              com.supply.api.user.UserProto.authenticationResponse>(
                service, METHODID_GET_USER_INFORMATION)))
        .build();
  }

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.supply.api.user.UserProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UserServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getGetUserInformationByIdMethod())
              .addMethod(getGetUserAuthorityMethod())
              .addMethod(getGetUserInformationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
