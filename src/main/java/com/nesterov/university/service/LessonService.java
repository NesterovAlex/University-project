package com.nesterov.university.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.model.Lesson;

@Component
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}
	
	public void createLesson(Lesson lesson){
		lessonDao.create(lesson);
	}
	
	public void deleteLesson(Lesson lesson){
		lessonDao.delete(lesson.getId());
	}
	
	public List<Lesson> getAllLessons(){
		return lessonDao.findAll();
	}
	
}
