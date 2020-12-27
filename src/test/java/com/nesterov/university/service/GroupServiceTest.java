package com.nesterov.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Optional.ofNullable;
import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueNameException;
import com.nesterov.university.dao.exceptions.NotUniqueRoomNumberException;
import com.nesterov.university.model.Group;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	@Mock
	private GroupDao groupDao;

	@InjectMocks
	private GroupService groupService;

	@Test
	void givenListOfExistsGroups_whenGetAll_thenExpectedListOfGroupsReturned() throws NotFoundEntitiesException {
		List<Group> groups = new ArrayList<>();
		groups.add(new Group(1, "G-12"));
		groups.add(new Group(2, "G-12"));
		groups.add(new Group(3, "G-12"));
		given(groupDao.findAll()).willReturn(groups);

		List<Group> actual = groupService.getAll();

		assertEquals(groups, actual);
	}

	@Test
	void givenEmptyListGroups_whenGetAll_thenNotFoundEntitiesExceptionThrown() throws NotFoundEntitiesException {
		given(groupDao.findAll()).willReturn(new ArrayList<>());

		assertThrows(NotFoundEntitiesException.class, () -> groupService.getAll());
	}

	@Test
	void givenGroup_whenGet_thenEqualGroupReturned() throws NotPresentEntityException {
		Group group = new Group(5, "G-12");
		given(groupDao.get(group.getId())).willReturn(ofNullable(group));

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
	void givenGroupId_whenDelete_thenDeleted() throws NotDeleteException {
		int groupId = 1;

		groupService.delete(groupId);

		verify(groupDao).delete(groupId);
	}

	@Test
	void givenNameGroup_whenUpdate_thenUpdated() throws NotCreateException, NotUniqueNameException {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(ofNullable(group));

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	void givenNameOfNonExistingGroup_whenUpdate_thenUpdated()
			throws EntityNotFoundException, NotCreateException, NotUniqueNameException {
		Group group = new Group(5, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(empty());

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	void givenExistingGroup_whenUpdate_thenNotCreated()
			throws EntityNotFoundException, NotCreateException, NotUniqueNameException {
		Group actual = new Group(5, "G-12");
		Group expected = new Group(4, "G-12");
		when(groupDao.findByName(actual.getName())).thenReturn(ofNullable(actual));

		assertThrows(NotUniqueNameException.class, () -> groupService.update(expected));

		verify(groupDao, never()).update(expected);
	}

	@Test
	void givenNonExistingGroup_whenCreate_thenCreated() throws NotCreateException, NotUniqueNameException {
		Group group = new Group(1, "G-12");
		when(groupDao.findByName(group.getName())).thenReturn(empty());

		groupService.create(group);

		verify(groupDao).create(group);
	}

	@Test
	void givenGroupWithDuplicatedName_whenCreate_thenNotCreated()
			throws NotCreateException, NotUniqueRoomNumberException {
		Group newGroup = new Group(2, "G-12");
		Group existingGroup = new Group(3, "G-12");
		when(groupDao.findByName(newGroup.getName())).thenReturn(ofNullable(existingGroup));

		assertThrows(NotUniqueNameException.class, () -> groupService.create(newGroup));

		verify(groupDao, never()).create(newGroup);
	}

	@Test
	void givenGroupWithDuplicatedName_whenCreate_thenNotUniqueRoomNumberExceptionThrown()
			throws NotCreateException, NotUniqueRoomNumberException {
		Group newGroup = new Group(2, "G-12");
		Group existingGroup = new Group(3, "G-12");
		when(groupDao.findByName(existingGroup.getName())).thenReturn(ofNullable(existingGroup));

		assertThrows(NotUniqueNameException.class, () -> groupService.create(newGroup));
	}
}