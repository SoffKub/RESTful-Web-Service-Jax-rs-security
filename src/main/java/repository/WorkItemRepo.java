package repository;

import model.User;
import model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkItemRepo extends CrudRepository<WorkItem, Long> {

	List<WorkItem> findAllByStatus(String status);

	List<WorkItem> findAllByUserId(long userId);

	List<WorkItem> findAllByUser(User user);

	List<WorkItem> findByDescriptionContaining(String text);

	List<WorkItem> findByTitleContaining(String text);

	@Query("select w from WorkItem w where w.user.team.name=:teamName")
	List<WorkItem> findAllByTeamName(@Param("teamName") String teamName);

}
