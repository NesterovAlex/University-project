package com.nesterov.university.service;

import org.springframework.stereotype.Component;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.model.Group;

@Component
public class GroupService {

	GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
	public void createGroup(Group group) {
		groupDao.create(group);
	}
	
	public void deleteGroup(Group group) {
		groupDao.delete(group.getId());
	}
}
