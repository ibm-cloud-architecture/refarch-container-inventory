package ibm.gse.app.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import ibm.gse.kc.model.Container;
import ibm.gse.service.ApplicationException;
import ibm.gse.service.ContainerService;

@Path("containers")
public class InventoryResource {
	
	@Inject
	protected ContainerService service;
	

	 public InventoryResource(ContainerService serv) {
		this.service = serv;
	}

	@GET
	 @Produces(MediaType.APPLICATION_JSON)
	 @Transactional
	 public Response getContainers(){
		    return Response.ok(service.getAllContainers()).build();
	 }
	 
	 @POST
	 @Consumes(MediaType.APPLICATION_JSON)
	 @Transactional
	 @Produces(MediaType.APPLICATION_JSON)
	 @Operation(summary = "Request to create an container", description = "")
	 @APIResponses(value = {
	    @APIResponse(responseCode = "400", description = "Bad create container request", content = @Content(mediaType = "text/plain")),
	    @APIResponse(responseCode = "200", description = "container created", content = @Content(mediaType = "application/json")) })	
	 public Response postContainer(Container container){
	        Container c = null;
	        try {
	    		 c=service.saveContainer(container);
	    		// JMS producer call
	    	} catch (ApplicationException e) {
	    		return Response.status(Status.CONFLICT).build();
	    	}
			return Response.ok().entity(c).build();
	 }
	 
	@GET
	@Path("{Id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Query a container by id", description = "")
	@APIResponses(value = {
            @APIResponse(responseCode = "404", description = "container not found", content = @Content(mediaType = "text/plain")),
            @APIResponse(responseCode = "200", description = "container found", content = @Content(mediaType = "application/json")) })
	public Response getById(@PathParam("Id") String containerID) {
		Optional<Container> oo = service.getById(containerID);
	    if (oo.isPresent()) {
	        return Response.ok().entity(oo.get()).build();
	    } else {
	        return Response.status(Status.NOT_FOUND).build();
	    }
	}

	
}
