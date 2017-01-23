package service;

import model.Status;
import model.Team;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import repository.TeamRepo;
import repository.UserRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeamService {
	@Autowired
	private TeamRepo teamRepository;
	@Autowired
	private UserRepo userRepository;

	public TeamService(TeamRepo teamRepository, UserRepo userRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
	}

	public TeamService() {
	}

	@Transactional
	public Team createTeam(Team team) {
		try {
			if (team.getId() == null) {
				team.setStatus(Status.ACTIVE.toString());
				return teamRepository.save(team);

			} else {
				throw new ServiceException("Team with this teamname already exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not add team " + team.getName() + ": " + e);
		}
	}

	public Iterable<Team> findAllTeams() {
		return teamRepository.findAll();
	}

	public Team findByName(String teamName) {
		Team team = teamRepository.findByName(teamName);
		return team;
	}

	public Team findOne(Long id) {
		Team team = teamRepository.findOne(id);
		return team;
	}

	@Transactional
	public void uppdateTeam(Long id, String newName) {
		try {
			Team team = teamRepository.findOne(id);

			Team newTeam = teamRepository.findByName(newName);
			if (newTeam == null) {
				team.setName(newName);
				teamRepository.save(team);
			} else {
				throw new ServiceException("Team with this team name " + newName + " exists");

			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not update team: " + e);
		}
	}

	@Transactional
	public void updateStatusTeam(Long id, Status status) {

		try {
			Team team = teamRepository.findOne(id);
			if (team != null) {
				team.setStatus(status.toString());
				teamRepository.save(team);
			} else {
				throw new ServiceException("Team with this team name NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not deactivate team with id " + id + ": " + e);
		}
	}

	@Transactional
	public void assigneUserToTeam(Long teamId, Long userId) {
		try {
			Team team = teamRepository.findOne(teamId);
			User user = userRepository.findOne(userId);
			if ((team != null) && (user != null)) {
				List<User> users = userRepository.findAllByTeam(team);
				if (users.size() < 10) {
					user.setTeam(team);
					userRepository.save(user);

				} else {
					throw new ServiceException(
							"This team already has 10 users! (But it is allowed to have MAX 10 users in one team)");
				}

			} else {
				throw new ServiceException("Team with this teamId NOT exists OR JUser NOT exists");
			}

		} catch (DataAccessException e) {
			throw new ServiceException("Could not update team: " + e);
		}
	}
}
