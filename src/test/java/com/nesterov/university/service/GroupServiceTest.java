package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(1, "G-12"));
		expected.add(new Group(2, "G-12"));
		expected.add(new Group(3, "G-12"));
		given(groupDao.findAll()).willReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
		verify(groupDao, times(1)).findAll();
	}

	@Test
	void givenExpectedGroup_whenGet_thenEqualGroupReturned() {
		final Group expected = new Group(5, "G-12");
		given(groupDao.get(anyLong())).willReturn(expected);

		final Group actual = groupService.get(anyLong());

		assertEquals(expected, actual);
		verify(groupDao, times(1)).get(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		int expected = 1;

		groupService.delete(anyLong());

		verify(groupDao, times(expected)).delete(anyLong());
	}

	@Test
	void givenExpectedCountOfDaoUpdateMethodCall_whenUpdate_thenEqualOfDaoUpdateMethodCallReturned() {
		int expected = 2;
		Group group = new Group(5, "G-12");
		doNothing().when(groupDao).update(any(Group.class));

		groupService.update(group);
		groupService.update(group);

		verify(groupDao, times(expected)).update(group);
	}

	@Test
	void givenExpectedListOfExistsGroups_whenFindByLessonId_thenRelevantListOfGroupsReturned() {
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(1, "G-12"));
		expected.add(new Group(2, "G-12"));
		expected.add(new Group(3, "G-12"));
		given(groupDao.findByLessonId(anyLong())).willReturn(expected);

		List<Group> actual = groupService.findByLessonId(anyLong());

		assertEquals(expected, actual);
		verify(groupDao, times(1)).findByLessonId(anyLong());
	}

	@Test
	void givenExpectedCountOfCreateDaoMethodCall_whenCreate_thenEqualOfDaoCreateMethodCallReturned() {
		int expected = 1;
		Group group = new Group(expected, "G-12");

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