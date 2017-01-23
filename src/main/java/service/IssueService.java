package service;

import model.Issue;
import model.WorkItem;
import model.WorkItemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import repository.IssueRepo;
import repository.WorkItemRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IssueService {

	@Autowired
	private IssueRepo issueRepository;
	@Autowired
	private WorkItemRepo workItemRepository;

	public IssueService() {
	}

	public IssueService(IssueRepo issueRepository, WorkItemRepo workItemRepository) {
		this.issueRepository = issueRepository;
		this.workItemRepository = workItemRepository;
	}

	@Transactional
	public Issue createIssue(Issue issue) throws ServiceException {
		if (issueRepository.findByDescription(issue.getDescription()) == null) {
			return issueRepository.save(issue);
		} else
			throw new ServiceException("Create issue failed. Issue: " + issue.getDescription() + " already exists.");
	}

	@Transactional
	public Issue createAndAssign(Issue issue, WorkItem workItem) {
		Issue newIssue = createIssue(issue);
		assignToWorkItem(newIssue, workItem);
		return newIssue;
	}

	@Transactional
	public void assignToWorkItem(Issue issue, WorkItem workItem) throws ServiceException {
		WorkItem newWorkItem = workItem;
		Issue newIssue = issue;
		try {
			if (issue.getId() == null) {
				newIssue = issueRepository.save(issue);
			}
			if (workItem.getId() == null) {
				newWorkItem = workItemRepository.save(workItem);
			}
			if (newWorkItem.getStatus().equals("Done")) {
				newWorkItem.setIssue(newIssue);
				newWorkItem.setStatus(WorkItemStatus.UNSTARTED.toString());
				workItemRepository.save(newWorkItem);
			} else {
				throw new ServiceException("Assign issue to work item failed. Status of work item is not 'Done'");
			}
		} catch (DataAccessException e) {
			throw new ServiceException("Couldn't assign issue to workitem: " + e);
		}

	}

	@Transactional
	public Issue updateIssue(Issue issue, String new_description) throws ServiceException {
		Issue findIssue = issueRepository.findByDescription(new_description);
		if (findIssue == null) {
			issue.setDescription(new_description);
			issueRepository.save(issue);
			return issue;
		} else
			throw new ServiceException("Issue with name:" + new_description + " already exists.");
	}

	public List<WorkItem> getAllItemsWithIssue(Issue issue) {
		return issueRepository.findAllByIssue(issue);
	}

	public Issue getIssueByName(String name) {
		return issueRepository.findByDescription(name);

	}

	public Issue getIssueById(Long id) {
		return issueRepository.findOne(id);
	}
}
