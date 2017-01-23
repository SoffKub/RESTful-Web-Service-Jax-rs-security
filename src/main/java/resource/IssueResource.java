package resource;

import model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.IssueService;
import service.ServiceException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Component
@Path("issues")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssueResource {
	@Autowired
	private IssueService issueService;

	@Context
	private UriInfo uriInfo;

	@POST
	@Secured
	public Response create(Issue issue) {
		try {
			issueService.createIssue(issue);
		} catch (ServiceException e) {
			throw new WebApplicationException("Issue already exists!", Status.CONFLICT);
		}
		URI location = uriInfo.getAbsolutePathBuilder().path(IssueResource.class, "update").build(issue.getId());
		return Response.created(location).build();
	}

	@PUT
	@Secured
	@Path("{id}")
	public Response update(Issue issue, @PathParam("id") Long id) {
		try {
			issueService.updateIssue(issueService.getIssueById(id), issue.getDescription());
		} catch (ServiceException e) {
			throw new WebApplicationException("Description already exists!", Status.CONFLICT);
		}
		return Response.ok(issue).build();
	}
}
