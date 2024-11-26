package com.supply.api.seckill;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 定义接口
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: SeckillProto.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SeckillServiceGrpc {

  private SeckillServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "SeckillService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest,
      com.google.protobuf.Empty> getStoreFlashSaleInformationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "storeFlashSaleInformation",
      requestType = com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest,
      com.google.protobuf.Empty> getStoreFlashSaleInformationMethod() {
    io.grpc.MethodDescriptor<com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest, com.google.protobuf.Empty> getStoreFlashSaleInformationMethod;
    if ((getStoreFlashSaleInformationMethod = SeckillServiceGrpc.getStoreFlashSaleInformationMethod) == null) {
      synchronized (SeckillServiceGrpc.class) {
        if ((getStoreFlashSaleInformationMethod = SeckillServiceGrpc.getStoreFlashSaleInformationMethod) == null) {
          SeckillServiceGrpc.getStoreFlashSaleInformationMethod = getStoreFlashSaleInformationMethod =
              io.grpc.MethodDescriptor.<com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "storeFlashSaleInformation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new SeckillServiceMethodDescriptorSupplier("storeFlashSaleInformation"))
              .build();
        }
      }
    }
    return getStoreFlashSaleInformationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.supply.api.seckill.SeckillProto.InventoryDeductionRequest,
      com.google.protobuf.Empty> getInventoryDeductionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "inventoryDeduction",
      requestType = com.supply.api.seckill.SeckillProto.InventoryDeductionRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.supply.api.seckill.SeckillProto.InventoryDeductionRequest,
      com.google.protobuf.Empty> getInventoryDeductionMethod() {
    io.grpc.MethodDescriptor<com.supply.api.seckill.SeckillProto.InventoryDeductionRequest, com.google.protobuf.Empty> getInventoryDeductionMethod;
    if ((getInventoryDeductionMethod = SeckillServiceGrpc.getInventoryDeductionMethod) == null) {
      synchronized (SeckillServiceGrpc.class) {
        if ((getInventoryDeductionMethod = SeckillServiceGrpc.getInventoryDeductionMethod) == null) {
          SeckillServiceGrpc.getInventoryDeductionMethod = getInventoryDeductionMethod =
              io.grpc.MethodDescriptor.<com.supply.api.seckill.SeckillProto.InventoryDeductionRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "inventoryDeduction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.supply.api.seckill.SeckillProto.InventoryDeductionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new SeckillServiceMethodDescriptorSupplier("inventoryDeduction"))
              .build();
        }
      }
    }
    return getInventoryDeductionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SeckillServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SeckillServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SeckillServiceStub>() {
        @java.lang.Override
        public SeckillServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SeckillServiceStub(channel, callOptions);
        }
      };
    return SeckillServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SeckillServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SeckillServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SeckillServiceBlockingStub>() {
        @java.lang.Override
        public SeckillServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SeckillServiceBlockingStub(channel, callOptions);
        }
      };
    return SeckillServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SeckillServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SeckillServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SeckillServiceFutureStub>() {
        @java.lang.Override
        public SeckillServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SeckillServiceFutureStub(channel, callOptions);
        }
      };
    return SeckillServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 定义接口
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void storeFlashSaleInformation(com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStoreFlashSaleInformationMethod(), responseObserver);
    }

    /**
     */
    default void inventoryDeduction(com.supply.api.seckill.SeckillProto.InventoryDeductionRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getInventoryDeductionMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SeckillService.
   * <pre>
   * 定义接口
   * </pre>
   */
  public static abstract class SeckillServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SeckillServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SeckillService.
   * <pre>
   * 定义接口
   * </pre>
   */
  public static final class SeckillServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SeckillServiceStub> {
    private SeckillServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SeckillServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SeckillServiceStub(channel, callOptions);
    }

    /**
     */
    public void storeFlashSaleInformation(com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStoreFlashSaleInformationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void inventoryDeduction(com.supply.api.seckill.SeckillProto.InventoryDeductionRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getInventoryDeductionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SeckillService.
   * <pre>
   * 定义接口
   * </pre>
   */
  public static final class SeckillServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SeckillServiceBlockingStub> {
    private SeckillServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SeckillServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SeckillServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.google.protobuf.Empty storeFlashSaleInformation(com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStoreFlashSaleInformationMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty inventoryDeduction(com.supply.api.seckill.SeckillProto.InventoryDeductionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getInventoryDeductionMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SeckillService.
   * <pre>
   * 定义接口
   * </pre>
   */
  public static final class SeckillServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SeckillServiceFutureStub> {
    private SeckillServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SeckillServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SeckillServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> storeFlashSaleInformation(
        com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStoreFlashSaleInformationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> inventoryDeduction(
        com.supply.api.seckill.SeckillProto.InventoryDeductionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getInventoryDeductionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_STORE_FLASH_SALE_INFORMATION = 0;
  private static final int METHODID_INVENTORY_DEDUCTION = 1;

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
        case METHODID_STORE_FLASH_SALE_INFORMATION:
          serviceImpl.storeFlashSaleInformation((com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_INVENTORY_DEDUCTION:
          serviceImpl.inventoryDeduction((com.supply.api.seckill.SeckillProto.InventoryDeductionRequest) request,
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
          getStoreFlashSaleInformationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.seckill.SeckillProto.StoreFlashSaleInformationRequest,
              com.google.protobuf.Empty>(
                service, METHODID_STORE_FLASH_SALE_INFORMATION)))
        .addMethod(
          getInventoryDeductionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.supply.api.seckill.SeckillProto.InventoryDeductionRequest,
              com.google.protobuf.Empty>(
                service, METHODID_INVENTORY_DEDUCTION)))
        .build();
  }

  private static abstract class SeckillServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SeckillServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.supply.api.seckill.SeckillProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SeckillService");
    }
  }

  private static final class SeckillServiceFileDescriptorSupplier
      extends SeckillServiceBaseDescriptorSupplier {
    SeckillServiceFileDescriptorSupplier() {}
  }

  private static final class SeckillServiceMethodDescriptorSupplier
      extends SeckillServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SeckillServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SeckillServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SeckillServiceFileDescriptorSupplier())
              .addMethod(getStoreFlashSaleInformationMethod())
              .addMethod(getInventoryDeductionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
