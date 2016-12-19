/*
 * Copyright 2016, TeamDev Ltd. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.spine3.examples.todolist.client;

import com.google.protobuf.Descriptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spine3.base.Command;
import org.spine3.base.Response;
import org.spine3.client.Subscription;
import org.spine3.client.SubscriptionUpdate;
import org.spine3.client.Target;
import org.spine3.client.Topic;
import org.spine3.client.grpc.CommandServiceGrpc;
import org.spine3.client.grpc.SubscriptionServiceGrpc;
import org.spine3.protobuf.Messages;
import org.spine3.protobuf.TypeUrl;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Sample gRPC client implementation.
 *
 * @author Illia Shepilov
 */
public class Client {

    private static final int TIMEOUT = 10;
    private final ManagedChannel channel;
    private final CommandServiceGrpc.CommandServiceBlockingStub blockingClient;
    private final SubscriptionServiceGrpc.SubscriptionServiceStub nonBlockingClient;
    private final StreamObserver<Subscription> streamObserver = new StreamObserver<Subscription>() {
        @Override
        public void onNext(Subscription value) {
            log().debug("Updated. Value is {}", value);
            nonBlockingClient.activate(value, observer);
        }

        @Override
        public void onError(Throwable t) {
            log().error("Subscription streaming error occurred", t);
        }

        @Override
        public void onCompleted() {
            log().info("Subscription stream completed.");
        }
    };
    private final StreamObserver<SubscriptionUpdate> observer = new StreamObserver<SubscriptionUpdate>() {
        @Override
        public void onNext(SubscriptionUpdate update) {
            final String updateText = Messages.toText(update);
            log().info(updateText);
        }

        @Override
        public void onError(Throwable throwable) {
            log().error("Streaming error occurred", throwable);
        }

        @Override
        public void onCompleted() {
            log().info("Stream completed.");
        }
    };

    /**
     * Construct the client connecting to server at {@code host:port}.
     */
    public Client(String host, int port) {
        this.channel = initChannel(host, port);
        this.blockingClient = CommandServiceGrpc.newBlockingStub(channel);
        this.nonBlockingClient = SubscriptionServiceGrpc.newStub(channel);
    }

    /**
     * Executes command.
     *
     * <p>Sends request to the server and obtains response from server.
     *
     * @param command {@link Command} instance
     * @return {@link Response} from server
     */
    public Response execute(Command command) {
        final Response result = post(command);
        return result;
    }

    private Response post(Command request) {
        Response result = null;
        try {
            result = blockingClient.post(request);
        } catch (RuntimeException e) {
            log().warn("failed", e);
            throw e;
        }
        return result;
    }

    /**
     * Subscribe topic to the client.
     *
     * <p> Takes {@link com.google.protobuf.Descriptors.Descriptor},
     * <p> subscribe {@link Topic} based on the descriptor to the {@code nonBlockingClient}.
     *
     * @param descriptor {@link com.google.protobuf.Descriptors.Descriptor} instance
     */
    public void subscribe(Descriptors.Descriptor descriptor) {
        final TypeUrl taskTypeUrl = TypeUrl.of(descriptor);
        final Target.Builder target = Target.newBuilder()
                                            .setType(taskTypeUrl.getTypeName());
        final Topic topic = Topic.newBuilder()
                                 .setTarget(target)
                                 .build();
        nonBlockingClient.subscribe(topic, streamObserver);
    }

    /**
     * Shutdown the connection channel.
     *
     * @throws InterruptedException if waiting is interrupted.
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown()
               .awaitTermination(TIMEOUT, SECONDS);
    }

    private ManagedChannel initChannel(String host, int port) {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build();
    }

    private enum LogSingleton {
        INSTANCE;
        @SuppressWarnings("NonSerializableFieldInSerializableClass")
        private final Logger value = LoggerFactory.getLogger(Client.class);
    }

    private static Logger log() {
        return LogSingleton.INSTANCE.value;
    }
}
