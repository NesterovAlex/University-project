package com.nesterov.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Teacher;

@Component
public class TeacherService {

	private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	public void create(Teacher teacher) {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			try {
				teacherDao.create(teacher);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public void delete(long id) {
		try {
			teacherDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public Teacher get(long id) {
		Teacher teacher = null;
		try {
			return teacherDao.get(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return teacher;
	}

	public void update(Teacher teacher) {
		if (isUniqueEmail(teacher) && isUniquePhone(teacher) && isUniqueAddress(teacher)) {
			try {
				teacherDao.update(teacher);
			} catch (EntityNotFoundException | NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<Teacher> getAll() {
		List<Teacher> teachers = null;
		try {
			return teacherDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return teachers;
	}

	public List<Teacher> findBySubjectId(long id) {
		List<Teacher> teachers = null;
		try {
			return teacherDao.findBySubjectId(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return teachers;
	}

	private boolean isUniqueEmail(Teacher teacher) {
		Teacher founded = null;
		try {
			founded = teacherDao.findByEmail(teacher.getEmail());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == teacher.getId();
	}

	private boolean isUniquePhone(Teacher teacher) {
		Teacher founded = null;
		try {
			founded = teacherDao.findByPhone(teacher.getPhone());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == teacher.getId();
	}

	private boolean isUniqueAddress(Teacher teacher) {
		Teacher founded = null;
		try {
			founded = teacherDao.findByAddress(teacher.getAddress());
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return founded == null || founded.getId() == teacher.getId();
	}
}
