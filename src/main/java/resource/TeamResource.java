package resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Status;
import model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.ServiceException;
import service.TeamService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Component
@Path("teams")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public final class TeamResource {

	@Autowired
	TeamService teamService;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response addTeam(Team team) {
		try {
			teamService.createTeam(team);
		} catch (ServiceException e) {
			throw new WebApplicationException("Team already exists!", javax.ws.rs.core.Response.Status.CONFLICT);
		}
		URI location = uriInfo.getAbsolutePathBuilder().path(TeamResource.class, "getTeam").build(team.getId());
		return Response.created(location).build();
	}

	@PUT
	@Path("{id}")
	public Response update(@PathParam("id") Long id, String reqBody) {
		JsonObject jobj = new Gson().fromJson(reqBody, JsonObject.class);

		if (jobj.has("status")) {

			String status = jobj.get("status").getAsString();
			try {
				Status.valueOf(status);
			} catch (IllegalArgumentException e) {
				return Response.status(400).type(MediaType.TEXT_PLAIN)
						.entity("Status has to be either of: ACTIVE or INACTIVE").build();
			}
			teamService.updateStatusTeam(id, Status.valueOf(status));
			return Response.ok(teamService.findOne(id)).build();
		}

		if (jobj.has("name")) {
			String teamName = jobj.get("name").getAsString();
			teamService.uppdateTeam(id, teamName);
			return Response.ok().build();

		}
		if (jobj.has("userId")) {
			Long userId = jobj.get("userId").getAsLong();
			teamService.assigneUserToTeam(id, userId);
			return Response.ok().build();

		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@GET
	public Response getAllTeams() {
		return Response.ok(teamService.findAllTeams()).build();
	}

	@GET
	@Path("{id}")
	public Response getTeam(@PathParam("id") Long id) {

		return Response.ok(teamService.findOne(id)).build();
	}

}