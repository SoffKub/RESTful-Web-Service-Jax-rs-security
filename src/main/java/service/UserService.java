package service;

import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TeamRepo;
import repository.UserRepo;
import repository.WorkItemRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepository;
	@Autowired
	private TeamRepo teamRepository;
	@Autowired
	private WorkItemRepo workItemRepository;

	public UserService(UserRepo userRepository, TeamRepo teamRepository,
					   WorkItemRepo workItemRepository) {
		this.userRepository = userRepository;
		this.teamRepository = teamRepository;
		this.workItemRepository = workItemRepository;
	}

	public UserService() {
	}

	public User getUser(Long Id) {
		return userRepository.findOne(Id);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User getUserByUsernumber(String usernumber) {
		return userRepository.findByUsernumber(usernumber);
	}

	public List<User> getUserByFirstname(String firstname) {
		return userRepository.findByFirstname(firstname);
	}

	public List<User> getUserByLastname(String lastname) {
		return userRepository.findByLastname(lastname);
	}

	public List<User> getAllUsersInTeam(Team team) {
		return userRepository.findAllByTeam(team);
	}


	@Transactional
	public User createUser(User user) {
		if (user.getUsername().length() >= 10) {
			teamRepository.save(user.getTeam());
			List<User> users = userRepository.findAllByTeam(user.getTeam());
			if (users.size() < 10) {
				return userRepository.save(user);

			} else {
				throw new ServiceException(
						"This team already has 10 users!");
			}

		} else
			throw new ServiceException("Username must be at least 10 characters long!");
	}

	@Transactional
	public User updateUser(User user) {
		if (user.getUsername().length() >= 10) {
			teamRepository.save(user.getTeam());
			List<User> users = userRepository.findAllByTeam(user.getTeam());
			if (users.size() < 10) {
				return userRepository.save(user);

			} else {
				throw new ServiceException(
						"This team is full!");
			}

		} else
			throw new ServiceException("Username must be at least 10 characters long!");
	}

	@Transactional
	public User deactivateUser(User user) {
		user.setStatus(Status.INACTIVE);
		List<WorkItem> workItems = workItemRepository.findAllByUser(user);
		for (WorkItem workItem : workItems) {
			workItem.setStatus(WorkItemStatus.UNSTARTED.toString());
		}
		return userRepository.save(user);
	}

	@Transactional
	public User activateUser(User user) {
		user.setStatus(Status.ACTIVE);
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserByToken(String token) {
		return userRepository.findByToken(token);
	}
}
