package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.NotRightTimeException;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeService {

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		verifyTimeIsRight(lessonTime);
		lessonTimeDao.create(lessonTime);
	}

	public void delete(long id) {
		if (!lessonTimeDao.get(id).isPresent()) {
			String message = format("LessonTime with id = '%s' not found", id);
			throw new NotFoundException(message);
		}
		lessonTimeDao.delete(id);
	}

	public LessonTime get(long id) {
		String message = format("LessonTime with id = '%s' not found", id);
		return lessonTimeDao.get(id).orElseThrow(() -> new NotFoundException(message));
	}

	public void update(LessonTime lessonTime) {
		verifyTimeIsRight(lessonTime);
		lessonTimeDao.update(lessonTime);
	}

	public List<LessonTime> getAll() {
		return lessonTimeDao.findAll();
	}

	private void verifyTimeIsRight(LessonTime lessonTime) {
		if (lessonTime.getStart().isAfter(lessonTime.getEnd())) {
			throw new NotRightTimeException("This is not right time of lesson");
		}
	}
}
