package com.nesterov.university.service;

import org.springframework.stereotype.Component;

import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeService {

	private LessonTimeDao lessonTimeDao;

	public LessonTimeService(LessonTimeDao lessonTimeDao) {
		this.lessonTimeDao = lessonTimeDao;
	}
	
	public void createLessonTime(LessonTime lessonTime) {
		lessonTimeDao.create(lessonTime);
	}
	
	public void deleteLessonTime(LessonTime lessonTime) {
		lessonTimeDao.delete(lessonTime.getId());
	}
}
