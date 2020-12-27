package com.nesterov.university.service;

import static java.lang.String.format;

import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.NotRightTimeException;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeService {

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) throws NotRightTimeException, NotCreateException {
		if (isRightTime(lessonTime)) {
			lessonTimeDao.create(lessonTime);
		}
	}

	public void delete(long id) throws NotDeleteException {
		lessonTimeDao.delete(id);
	}

	public LessonTime get(long id) throws NotPresentEntityException {
		String message = format("LessonTime with id = '%s' not found", id);
		return lessonTimeDao.get(id).orElseThrow(() -> new NotPresentEntityException(message));
	}

	public void update(LessonTime lessonTime) throws NotRightTimeException {
		if (isRightTime(lessonTime)) {
			lessonTimeDao.update(lessonTime);
		}
	}

	public List<LessonTime> getAll() throws NotFoundEntitiesException {
		List<LessonTime> lessonTimes = lessonTimeDao.findAll();
		if (lessonTimes.isEmpty()) {
			throw new NotFoundEntitiesException("Lessontimes not found");
		}
		return lessonTimes;
	}

	private boolean isRightTime(LessonTime lessonTime) throws NotRightTimeException {
		if (lessonTime.getStart().isAfter(lessonTime.getEnd())) {
			throw new NotRightTimeException("This is not right time of lesson");
		}
		return lessonTime.getStart().isBefore(lessonTime.getEnd());
	}
}
