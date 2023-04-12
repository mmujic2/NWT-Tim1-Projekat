package com.example.demo;

import io.grpc.stub.StreamObserver;
import jakarta.persistence.Entity;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;


@GrpcService
public class EventService extends EventServiceGrpc.EventServiceImplBase {
    @Override
    public void logevent(com.example.demo.EventRequest request,
                         io.grpc.stub.StreamObserver<com.example.demo.EventResponse> responseObserver) {

        FoodieEvent newEvent = new FoodieEvent(request.getTimestamp(),
                request.getAction(),
                request.getEvent(),
                request.getServiceName(),
                request.getUser());

        RepositoryContainer.foodieEventRepository.save(newEvent);

        EventResponse response = EventResponse.newBuilder().setResponse("Success").build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
