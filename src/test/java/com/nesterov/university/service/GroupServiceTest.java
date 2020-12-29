package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotFoundException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueNameException;
import com.nesterov.university.model.Group;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@Mock
	private GroupDao groupDao;

	@InjectMocks
	private GroupService groupService;

	@Test
	void givenListOfExistsGroups_whenGetAll_thenExpectedListOfGroupsReturned() {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "G-12"));
		groups.add(new Group(2, "G-12"));
		groups.add(new Group(3, "G-12"));
		given(groupDao.findAll()).willReturn(groups);

		List<Group> actual = groupService.getAll();

		assertEquals(groups, actual);
	}

	@Test
	void givenEmptyListGroups_whenGetAll_thenNotFoundEntitiesExceptionThrown() {
		given(groupDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> groupService.getAll());
	}

	@Test
	void givenGroup_whenGet_thenEqualGroupReturned() {
		Group group = new Group(5, "G-12");
		given(groupDao.get(group.getId())).willReturn(of(group));

		Group actual = groupService.get(group.getId());

		assertEquals(group, actual);
	}

	@Test
	void givenEmptyOption_whenGet_thenNotPresentEntityExceptionThrown() {
		Group group = new Group(3, "K-12");
		given(groupDao.get(group.getId())).willReturn(empty());

		assertThrows(NotPresentEntityException.class, () -> groupService.get(group.getId()));
	}

	@Test
	void givenGroupId_whenDelete_thenDeleted() {
		Group group = new Group(7, "C-42");
		when(groupDao.get(group.getId())).thenReturn(of(group));

		groupService.delete(group.getId());

		verify(groupDao).delete(group.getId());
	}

	@Test
	void givenOptionalEmpty_whenDelete_thenNotFoundExceptionThrown() {
		Group group = new Group(4, "W-33");
		when(groupDao.get(group.getId())).thenReturn(empty());

		assertThrows(NotFoundException.class, () -> groupService.delete(group.getId()));
	}

	@Test
	void givenNameGroup_whenUpdate_thenUpdated() {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(of(group));

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	void givenNameOfNonExistingGroup_whenUpdate_thenUpdated() {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(of(group));

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	void givenExistingGroup_whenUpdate_thenNotCreated() {
		Group actual = new Group(5, "G-12");
		Group expected = new Group(4, "G-12");
		when(groupDao.findByName(actual.getName())).thenReturn(empty());

		assertThrows(NotUniqueNameException.class, () -> groupService.update(expected));

		verify(groupDao, never()).update(expected);
	}

	@Test
	void givenNonExistingGroup_whenCreate_thenCreated() {
		Group group = new Group(1, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(of(group));

		groupService.create(group);

		verify(groupDao).create(group);
	}

	@Test
	void givenGroupWithDuplicatedName_whenCreate_thenNotCreated() {
		Group newGroup = new Group(2, "G-12");
		when(groupDao.findByName(newGroup.getName())).thenReturn(empty());

		assertThrows(NotUniqueNameException.class, () -> groupService.create(newGroup));

		verify(groupDao, never()).create(newGroup);
	}

	@Test
	void givenGroupWithDuplicatedName_whenCreate_thenNotUniqueRoomNumberExceptionThrown() {
		Group newGroup = new Group(2, "G-12");
		Group existingGroup = new Group(3, "G-12");
		when(groupDao.findByName(existingGroup.getName())).thenReturn(empty());

		assertThrows(NotUniqueNameException.class, () -> groupService.create(newGroup));
	}
}