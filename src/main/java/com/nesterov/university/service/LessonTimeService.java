package com.nesterov.university.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeService {

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		if (!existsById(lessonTime.getId()))
			lessonTimeDao.create(lessonTime);
	}

	public void delete(long id) {
		lessonTimeDao.delete(id);
	}

	public LessonTime get(long id) {
		return lessonTimeDao.get(id);
	}

	public void update(LessonTime lessonTime) {
		lessonTimeDao.update(lessonTime);
	}

	public List<LessonTime> getAll() {
		return lessonTimeDao.getAll();
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			lessonTimeDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
