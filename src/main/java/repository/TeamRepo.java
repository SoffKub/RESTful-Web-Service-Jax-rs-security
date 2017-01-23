package repository;

import model.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepo extends CrudRepository<Team, Long> {

	Team findByName(String name);

	Team findOne(Long id);

}