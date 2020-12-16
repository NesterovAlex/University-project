package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.model.Group;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@Mock
	private GroupDao groupDao;

	@InjectMocks
	private GroupService groupService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void givenExpectedListOfExistsGroups_whenGetAll_thenRelevantListOfGroupsReturned() {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "G-12"));
		groups.add(new Group(2, "G-12"));
		groups.add(new Group(3, "G-12"));
		given(groupDao.findAll()).willReturn(groups);

		List<Group> actual = groupService.getAll();

		assertEquals(groups, actual);
		verify(groupDao).findAll();
	}

	@Test
	void givenExpectedGroup_whenGet_thenEqualGroupReturned() {
		Group group = new Group(5, "G-12");
		given(groupDao.get(group.getId())).willReturn(group);

		final Group actual = groupService.get(group.getId());

		assertEquals(group, actual);
		verify(groupDao).get(group.getId());
	}

	@Test
	void givenExpectedIdOfGroup_whenDelete_thenDeleted() {
		int expected = 1;

		groupService.delete(expected);

		verify(groupDao).delete(expected);
	}

	@Test
	void givenExpectedNameOfExistingGroup_whenUpdate_thenUpdated() {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(group);

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	void givenNameOfNonExistingGroup_whenUpdate_thenUpdated() {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(null);

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	void givenExistingGroup_whenUpdate_thenNotCreated() {
		Group actual = new Group(5, "G-12");
		Group expected = new Group(4, "G-12");
		when(groupDao.findByName(actual.getName())).thenReturn(actual);

		groupService.update(expected);

		verify(groupDao, never()).update(expected);
	}

	@Test
	void givenNonExistingGroup_whenCreate_thenCreated() {
		Group group = new Group(1, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(null);

		groupService.create(group);

		verify(groupDao).create(group);
	}

	@Test
	void givenGroupWithDuplicatedName_whenCreate_thenNotCreated() {
		Group expected = new Group(2, "G-12");
		Group actual = new Group(3, "G-12");
		when(groupDao.findByName(actual.getName())).thenReturn(actual);

		groupService.create(expected);

		verify(groupDao, never()).create(expected);
	}
}