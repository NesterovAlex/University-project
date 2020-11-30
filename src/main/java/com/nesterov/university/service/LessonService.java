package com.nesterov.university.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.model.Lesson;

@Component
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		if (!existsById(lesson.getId()))
			lessonDao.create(lesson);
	}

	public void delete(long id) {
		lessonDao.delete(id);
	}

	public Lesson get(long id) {
		return lessonDao.get(id);
	}

	public void update(Lesson subject) {
		lessonDao.update(subject);
	}

	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	private boolean existsById(long id) {
		boolean exist;
		try {
			lessonDao.get(id);
			exist = true;
		} catch (EmptyResultDataAccessException e) {
			exist = false;
		}
		return exist;
	}
}
