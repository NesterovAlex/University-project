package com.nesterov.university.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeService {

	private static final Logger log = LoggerFactory.getLogger(LessonTimeService.class);

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}

	public void create(LessonTime lessonTime) {
		if (isRightTime(lessonTime)) {
			try {
				lessonTimeDao.create(lessonTime);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public void delete(long id) {
		try {
			lessonTimeDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public LessonTime get(long id) {
		LessonTime lessonTime = null;
		try {
			return lessonTimeDao.get(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return lessonTime;
	}

	public void update(LessonTime lessonTime) {
		if (isRightTime(lessonTime)) {
			try {
				lessonTimeDao.update(lessonTime);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<LessonTime> getAll() {
		List<LessonTime> lessonTimes = null;
		try {
			return lessonTimeDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return lessonTimes;
	}

	private boolean isRightTime(LessonTime lessonTime) {
		return lessonTime.getStart().isBefore(lessonTime.getEnd());
	}
}
