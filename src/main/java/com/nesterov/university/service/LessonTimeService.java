package com.nesterov.university.service;

import java.util.List;
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
		if (isRightTime(lessonTime)) {
			lessonTimeDao.create(lessonTime);
		}
	}

	public void delete(long id) {
		lessonTimeDao.delete(id);
	}

	public LessonTime get(long id) {
		return lessonTimeDao.get(id);
	}

	public void update(LessonTime lessonTime) {
		if (isRightTime(lessonTime)) {
			lessonTimeDao.update(lessonTime);
		}
	}

	public List<LessonTime> getAll() {
		return lessonTimeDao.findAll();
	}
	
	private boolean isRightTime(LessonTime lessonTime) {
		return lessonTime.getStart().isBefore(lessonTime.getEnd());
	}
}
