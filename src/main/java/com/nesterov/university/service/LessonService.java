package com.nesterov.university.service;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.model.Lesson;

@Component
public class LessonService {

	private static final Logger log = LoggerFactory.getLogger(LessonService.class);

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			try {
				lessonDao.create(lesson);
			} catch (NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public void delete(long id) {
		try {
			lessonDao.delete(id);
		} catch (NotExistException e) {
			log.error(e.toString());
		}
	}

	public Lesson get(long id) {
		Lesson lesson = null;
		try {
			return lessonDao.get(id);
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return lesson;
	}

	public void update(Lesson lesson) {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			try {
				lessonDao.update(lesson);
			} catch (EntityNotFoundException | NotCreateException e) {
				log.error(e.toString());
			}
		}
	}

	public List<Lesson> getAll() {
		List<Lesson> lessons = null;
		try {
			lessons = lessonDao.findAll();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return lessons;
	}

	private boolean hasLessonsWithSameGroups(Lesson lesson) {
		boolean result = false;
		try {
			result = !lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId()).isEmpty();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return result;
	}

	private boolean hasLessonsWithSameTeacher(Lesson lesson) {
		boolean result = false;
		try {
			result = !lessonDao
					.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId())
					.isEmpty();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return result;
	}

	private boolean hasRightToTeach(Lesson lesson) {
		return lesson.getTeacher().getSubjects().stream().anyMatch(s -> s.equals(lesson.getSubject()));
	}

	private boolean isEmptyAudience(Lesson lesson) {
		boolean result = false;
		try {
			result = lessonDao
					.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId())
					.isEmpty();
		} catch (EntityNotFoundException e) {
			log.error(e.toString());
		}
		return result;
	}

	private boolean hasEnoughPlaces(Lesson lesson) {
		return countStudentsOfLesson(lesson) <= lesson.getAudience().getCapacity();
	}

	private long countStudentsOfLesson(Lesson lesson) {
		return lesson.getGroups().stream().mapToLong(g -> g.getStudents().size()).sum();
	}

	private boolean isWeekend(Lesson lesson) {
		return lesson.getDate().getDayOfWeek() == DayOfWeek.SATURDAY
				|| lesson.getDate().getDayOfWeek() == DayOfWeek.SUNDAY;
	}
}
