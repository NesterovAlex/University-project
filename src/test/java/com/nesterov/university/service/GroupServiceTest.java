package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.model.Group;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@Mock
	private GroupDao groupDao;

	@InjectMocks
	private GroupService groupService;

	@Spy
	List<Group> groups = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		groups.add(new Group(5, "G-12"));
	}

	@Test
	void givenExpectedListOfExistsGroups_whenGetAll_thenRelevantListOfGroupsReturned() {
		List<Group> expected = new ArrayList<>();
		expected.add(new Group(1, "G-12"));
		expected.add(new Group(2, "G-12"));
		expected.add(new Group(3, "G-12"));
		given(groupDao.getAll()).willReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedGroup_whenGet_thenEqualGroupReturned() {
		final Group expected = new Group(5, "G-12");
		given(groupDao.get(anyLong())).willReturn(expected);

		final Group actual = groupService.get(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	void givenExpectedCountOfDaoDeleteMethodCall_whenDelete_thenEqualOfDaoDeleteMethodCallReturned() {
		doNothing().when(groupDao).delete(anyLong());

		groupService.delete(anyLong());

		verify(groupDao, times(1)).delete(anyLong());
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
	}

	@Test
	void givenExpectedCountOfDaoMethodCall_whenCreate_EqualOfDaoMethodCallReturned() {
		int expected = 5;
		Group group = new Group(expected, "G-12");
		doThrow(new EmptyResultDataAccessException(1)).when(groupDao).get(expected);

		groupService.create(groups.get(0));

		verify(groupDao, times(1)).create(group);
	}
}