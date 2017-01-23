package resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jersey.model.JUser;
import model.Team;
import model.User;
import model.WorkItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.TeamService;
import service.UserService;
import service.WorkItemService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("users")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
@Consumes(MediaType.APPLICATION_JSON)
public final class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private WorkItemService workItemService;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response addUser(JUser jerseyUser) {

		String teamname = jerseyUser.getTeamname();
		Team team = teamService.findByName(teamname);
		if (team == null) {
			team = new Team(teamname);
		}
		User newUser = new User(jerseyUser.getFirstname(), jerseyUser.getLastname(), jerseyUser.getUsername(), jerseyUser.getPassword(), team);
		newUser.generateUsernumber();
		newUser = userService.createUser(newUser);
		URI location = uriInfo.getAbsolutePathBuilder().path(UserResource.class, "getSingleUser").build(newUser.getId());
		return Response.created(location).build();

	}

	@GET
	@Secured
	public Response getUser(@QueryParam("filter") String filter, @QueryParam("criteria") String criteria) {

		List<User> users = new ArrayList<>();
		User user = null;

		if (filter == null) {
			users = userService.getAllUsers();
			return Response.ok(users).build();
		}

		switch (filter) {
			case "id":
				user = userService.getUser(Long.parseLong(criteria));
				if (user != null) users.add(user);
				break;
			case "username":
				user = userService.getUserByUsername(criteria);
				if (user != null) users.add(user);
				break;
			case "usernumber":
				user = userService.getUserByUsernumber(criteria);
				if (user != null) users.add(user);
				break;
			case "fname":
				users = userService.getUserByFirstname(criteria);
				break;
			case "lname":
				users = userService.getUserByLastname(criteria);
				break;
			case "teamname":
				Team team = teamService.findByName(criteria);
				users = userService.getAllUsersInTeam(team);
				break;
			default:
				return Response.status(Status.BAD_REQUEST).entity("Query parameter format is wrong.").build();
		}
		if (users.size() == 0)
			return Response.status(Status.NOT_FOUND).entity("User with given criteria doesn't exist.").build();
		else {
			return Response.ok(users).build();
		}
	}

	@GET
	@Secured
	@Path("{id}")
	public Response getSingleUser(@PathParam("id") @DefaultValue("1") Long id) {

		User user = userService.getUser(id);
		return Response.ok(user).build();
	}

	@PUT
	@Path("{id}")
	@Secured
	public Response updateUser(@PathParam("id") String stringId, String reqBody) {

		long id = Long.parseLong(stringId);
		JsonObject jobj = new Gson().fromJson(reqBody, JsonObject.class);
		User user = userService.getUser(id);

		if (jobj.has("workItemId")) {

			Long workItemId = jobj.get("workItemId").getAsLong();
			WorkItem workItem = workItemService.findById(workItemId);
			workItemService.addWorkItemToUser(workItem, user);
			return Response.ok().build();

		} else if (jobj.has("deactivate") && jobj.get("deactivate").getAsBoolean()) {

			userService.deactivateUser(user);
			return Response.ok().build();

		} else if (jobj.has("activate") && jobj.get("activate").getAsBoolean()) {

			userService.activateUser(user);
			return Response.ok().build();

		} else if (jobj.has("update") && jobj.get("update").getAsBoolean()) {

			String firstname = jobj.get("firstname").getAsString();
			String lastname = jobj.get("lastname").getAsString();
			String username = jobj.get("username").getAsString();
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setUsername(username);
			userService.updateUser(user);
			return Response.ok().build();

		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
