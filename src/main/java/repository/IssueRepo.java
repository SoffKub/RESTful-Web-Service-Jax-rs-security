package repository;

import model.Issue;
import model.WorkItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepo extends CrudRepository<Issue, Long> {

	Issue findByDescription(String description);

	@Query("select w from WorkItem w where w.issue =:issue")
	List<WorkItem> findAllByIssue(@Param("issue") Issue issue);

}
