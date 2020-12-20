package com.nesterov.university.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Group;

@Component
public class GroupService {

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void create(Group group) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueName(group)) {
			groupDao.create(group);
		}
	}

	public void delete(long id) throws NotExistException {
		groupDao.delete(id);
	}

	public Group get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return groupDao.get(id);
	}

	public void update(Group group) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueName(group)) {
			groupDao.update(group);
		}
	}

	public List<Group> getAll() throws EntityNotFoundException, QueryNotExecuteException {
		return groupDao.findAll();
	}

	private boolean isUniqueName(Group group) throws EntityNotFoundException, QueryNotExecuteException {
		Group founded = groupDao.findByName(group.getName());
		return founded == null || founded.getId() == group.getId();
	}
}
