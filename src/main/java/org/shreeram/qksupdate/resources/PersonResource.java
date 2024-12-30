package org.shreeram.qksupdate.resources;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.shreeram.qksupdate.entity.Person;
import org.shreeram.qksupdate.service.MyBusiness;

import java.util.List;

@Path("/Upgrade")
public class PersonResource {

    @Inject
    private MyBusiness myBusinsess;

    @GET
    @Path("/persons")
    public Response search(){
        List<Person> list = myBusinsess.search();
        return Response.ok(list).build();
    }

    @POST
    @Path("/persons")
    public Response add(org.shreeram.qksupdate.domain.Person person){
        return myBusinsess.add(person)? Response.status(201).build():Response.status(Response.Status.EXPECTATION_FAILED).build();
    }
}
