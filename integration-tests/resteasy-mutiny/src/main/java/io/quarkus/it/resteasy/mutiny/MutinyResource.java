package io.quarkus.it.resteasy.mutiny;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.annotations.SseElementType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/mutiny")
public class MutinyResource {

    @Inject
    SomeService service;

    @GET
    @Path("/hello")
    public Uni<String> hello() {
        return service.greeting();
    }

    @GET
    @Path("/fail")
    public Uni<String> fail() {
        return Uni.createFrom().failure(new IOException("boom"));
    }

    @GET
    @Path("/response")
    public Uni<Response> response() {
        return service.greeting()
                .onItem().transform(v -> Response.accepted().type("text/plain").entity(v).build());
    }

    @GET
    @Path("/hello/stream")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<String> helloAsMulti() {
        return service.greetingAsMulti();
    }

    @GET
    @Path("/pet")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Pet> pet() {
        return service.getPet();
    }

    @GET
    @Path("/pet/stream")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Pet> pets() {
        return service.getPets();
    }

    @GET
    @Path("/pets")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<Pet> sse() {
        System.out.println(new Date() + " Called Pets");
        return service.getMorePets();
    }

    @Inject
    @RestClient
    MyRestService client;

    @GET
    @Path("/client")
    public Uni<String> callHello() {
        return client.hello();
    }

    @GET
    @Path("/client/pet")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Pet> callPet() {
        return client.pet();
    }

}
