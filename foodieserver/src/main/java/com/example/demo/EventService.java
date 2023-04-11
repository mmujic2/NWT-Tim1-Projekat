package com.example.demo;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;


@GrpcService
public class EventService extends EventServiceGrpc.EventServiceImplBase {

    @Override
    public void logevent(com.example.demo.EventRequest request,
                         io.grpc.stub.StreamObserver<com.example.demo.EventResponse> responseObserver) {
        EventResponse response = EventResponse.newBuilder().setResponse("Test message for client").build();
        System.out.println("Maybe");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
