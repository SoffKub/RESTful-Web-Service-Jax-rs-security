package repository;

import model.Team;
import model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {

	User findByUsername(String username);

	User findByUsernumber(String usernumber);

	List<User> findByFirstname(String firstname);

	List<User> findByLastname(String lastname);

	List<User> findAllByTeam(Team team);

	List<User> findAll();

	User findByToken(String token);

}
