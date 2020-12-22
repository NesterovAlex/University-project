package com.nesterov.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Group;

@Component
public class GroupService {

	private static final Logger log = LoggerFactory.getLogger(GroupService.class);

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	public void create(Group group) {
		if (isUniqueName(group)) {
			try {
				groupDao.create(group);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public void delete(long id) {
		try {
			groupDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public Group get(long id) {
		Group group = null;
		try {
			group = groupDao.get(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return group;
	}

	public void update(Group group) {
		if (isUniqueName(group)) {
			try {
				groupDao.update(group);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<Group> getAll() {
		List<Group> groups = null;
		try {
			groups = groupDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return groups;
	}

	private boolean isUniqueName(Group group) {
		Group founded = null;
		try {
			founded = groupDao.findByName(group.getName());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == group.getId();
	}
}
