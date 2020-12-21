package com.nesterov.university.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			teacherDao.create(teacher);
		}
	}

	public void delete(long id) throws NotExistException {
		teacherDao.delete(id);
	}

	public Teacher get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return teacherDao.get(id);
	}

	public void update(Teacher teacher) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			teacherDao.update(teacher);
		}
	}

	public List<Teacher> getAll() throws EntityNotFoundException, QueryNotExecuteException {
		return teacherDao.findAll();
	}

	public List<Teacher> findBySubjectId(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return teacherDao.findBySubjectId(id);
	}

	private boolean isUniqueEmail(Teacher teacher) throws EntityNotFoundException, QueryNotExecuteException {
		Teacher founded = teacherDao.findByEmail(teacher.getEmail());
		return founded == null || founded.getId() == teacher.getId();
	}

	private boolean isUniquePhone(Teacher teacher) throws EntityNotFoundException, QueryNotExecuteException {
		Teacher founded = teacherDao.findByPhone(teacher.getPhone());
		return founded == null || founded.getId() == teacher.getId();
	}

	private boolean isUniqueAddress(Teacher teacher) throws EntityNotFoundException, QueryNotExecuteException {
		Teacher founded = teacherDao.findByAddress(teacher.getAddress());
		return founded == null || founded.getId() == teacher.getId();
	}
}
