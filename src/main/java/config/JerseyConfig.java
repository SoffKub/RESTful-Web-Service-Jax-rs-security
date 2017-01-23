package config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import resource.*;

@Component
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {

		register(UserResource.class);
		register(WorkItemResource.class);
		register(TeamResource.class);
		register(IssueResource.class);
		register(AuthenticationResource.class);
		register(CustomerRequestFilter.class);

	}
}
