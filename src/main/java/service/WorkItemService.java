package service;

import model.Status;
import model.User;
import model.WorkItem;
import model.WorkItemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.WorkItemRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class WorkItemService {
	@Autowired
	private WorkItemRepo workItemRepository;

	public WorkItemService() {
	}

	public WorkItemService(WorkItemRepo workItemRepository) {
		this.workItemRepository = workItemRepository;
	}

	public WorkItem findById(Long id) {
		return workItemRepository.findOne(id);
	}

	public List<WorkItem> findAllByTeamName(String name) {
		return workItemRepository.findAllByTeamName(name);
	}


	public List<WorkItem> findAllByStatus(WorkItemStatus status) {
		return this.workItemRepository.findAllByStatus(status.toString());
	}


	public List<WorkItem> findAllByUser(Long userId) {
		return this.workItemRepository.findAllByUserId(userId);
	}


	public List<WorkItem> findAllByUser(User user) {
		return this.workItemRepository.findAllByUser(user);
	}

	@Transactional
	public WorkItem create(WorkItem workItem) {
		return this.workItemRepository.save(workItem);
	}

	@Transactional
	public WorkItem updateStatus(Long id, WorkItemStatus status) {
		WorkItem workItem = workItemRepository.findOne(id);
		if (workItem == null)
			throw new ServiceException("Update status failed. WorkItem with id:" + id + " is null");
		workItem.setStatus(status.toString());
		workItemRepository.save(workItem);
		return workItem;
	}

	@Transactional
	public WorkItem delete(Long id) {
		WorkItem workItem = workItemRepository.findOne(id);
		if (workItem != null)
			workItemRepository.delete(workItem);
		return workItem;

	}

	public WorkItem delete(WorkItem workItem) {
		return delete(workItem.getId());
	}

	public List<WorkItem> findByTitleContaining(String text) {
		return workItemRepository.findByTitleContaining(text);
	}

	public List<WorkItem> findByDescriptionContaining(String text) {
		return workItemRepository.findByDescriptionContaining(text);
	}

	@Transactional
	public void addWorkItemToUser(WorkItem workItem, User user) {
		if (user.getStatus().equals(Status.ACTIVE.toString()) && checkNumberofWorkItems(user)) {
			workItem.setUser(user);
			this.workItemRepository.save(workItem);
		} else {
			throw new ServiceException("Can not add a user to work item. JUser is not active or already has more than 5 work items.");
		}
	}

	public boolean checkNumberofWorkItems(User user) {
		return this.workItemRepository.findAllByUser(user).size() < 5;
	}
}
