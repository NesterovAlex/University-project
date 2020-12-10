package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
		int expected = 1;
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "G-12"));
		groups.add(new Group(2, "G-12"));
		groups.add(new Group(3, "G-12"));
		given(groupDao.findAll()).willReturn(groups);

		List<Group> actual = groupService.getAll();

		assertEquals(groups, actual);
		verify(groupDao, times(expected)).findAll();
	}

	@Test
	void givenExpectedGroup_whenGet_thenEqualGroupReturned() {
		int expected = 1;
		Group group = new Group(5, "G-12");
		given(groupDao.get(group.getId())).willReturn(group);

		final Group actual = groupService.get(group.getId());

		assertEquals(group, actual);
		verify(groupDao, times(expected)).get(group.getId());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		groupService.delete(expected);

		verify(groupDao, times(expected)).delete(expected);
	}

	@Test
	void givenExpectedNameOfExistingGroup_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 1;
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(group);

		groupService.update(group);

		verify(groupDao, times(expected)).update(group);
	}

	@Test
	void givenNonExistingGroup_whenUpdate_thenDontCallDaoUpdateMethod() {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(new Group());

		groupService.update(group);

		verify(groupDao, never()).update(group);
	}

	@Test
	void givenExpectedCountOfCreateDaoMethodCall_whenCreate_thenEqualOfDaoCreateMethodCallReturned() {
		int expected = 1;
		Group group = new Group(expected, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(new Group());
		
		groupService.create(group);

		verify(groupDao, times(expected)).create(group);
	}

	@Test
	void givenExistingGroup_whenCreate_thenDontCallDaoCreateMethod() {
		int expected = 5;
		Group group = new Group(expected, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(group);

		groupService.create(group);

		verify(groupDao, never()).create(group);
	}
}