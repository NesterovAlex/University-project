package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotUniqueAddressException;
import com.nesterov.university.dao.exceptions.NotUniqueEmailException;
import com.nesterov.university.dao.exceptions.NotUniquePhoneException;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher)
			throws NotUniqueEmailException, NotCreateException, NotUniquePhoneException, NotUniqueAddressException {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			teacherDao.create(teacher);
		}
	}

	public void delete(long id) throws NotDeleteException {
		teacherDao.delete(id);
	}

	public Teacher get(long id) throws NotPresentEntityException {
		Optional<Teacher> teacherOptional = teacherDao.get(id);
		if (!teacherOptional.isPresent()) {
			String message = format("Teacher with id = '%s' not found", id);
			throw new NotPresentEntityException(message);
		}
		return teacherOptional.orElse(new Teacher());
	}

	public void update(Teacher teacher)
			throws NotUniqueEmailException, NotUniquePhoneException, NotUniqueAddressException {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			teacherDao.update(teacher);
		}
	}

	public List<Teacher> getAll() throws NotFoundEntitiesException {
		List<Teacher> teachers = teacherDao.findAll();
		if (teachers.isEmpty()) {
			throw new NotFoundEntitiesException("Teachers not found");
		}
		return teachers;
	}

	public List<Teacher> findBySubjectId(long id) throws NotFoundEntitiesException {
		List<Teacher> teachers = teacherDao.findBySubjectId(id);
		if (teachers.isEmpty()) {
			String message = format("Teachers with subjectId = '%s' not found", id);
			throw new NotFoundEntitiesException(message);
		}
		return teachers;
	}

	private boolean isUniqueEmail(Teacher teacher) throws NotUniqueEmailException {
		Optional<Teacher> founded = teacherDao.findByEmail(teacher.getEmail());
		if (founded.isPresent() && founded.orElse(new Teacher()).getId() != teacher.getId()) {
			String message = format("Teacher email = '%s' not unique", teacher.getEmail());
			throw new NotUniqueEmailException(message);
		}
		return !founded.isPresent() || founded.orElse(new Teacher()).getId() == teacher.getId();
	}

	private boolean isUniquePhone(Teacher teacher) throws NotUniquePhoneException {
		Optional<Teacher> founded = teacherDao.findByPhone(teacher.getPhone());
		if (founded.isPresent() && founded.orElse(new Teacher()).getId() != teacher.getId()) {
			String message = format("Teacher phone = '%s' not unique", teacher.getPhone());
			throw new NotUniquePhoneException(message);
		}
		return !founded.isPresent() || founded.orElse(new Teacher()).getId() == teacher.getId();
	}

	private boolean isUniqueAddress(Teacher teacher) throws NotUniqueAddressException {
		Optional<Teacher> founded = teacherDao.findByAddress(teacher.getAddress());
		if (founded.isPresent() && founded.orElse(new Teacher()).getId() != teacher.getId()) {
			String message = format("Teacher address = '%s' not unoque", teacher.getAddress());
			throw new NotUniqueAddressException(message);
		}
		return !founded.isPresent() || founded.orElse(new Teacher()).getId() == teacher.getId();
	}
}
