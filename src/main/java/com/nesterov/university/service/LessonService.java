package com.nesterov.university.service;

import java.time.DayOfWeek;
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

	public void create(Lesson lesson) {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public void delete(long id) {
		lessonDao.delete(id);
	}

	public Lesson get(long id) {
		return lessonDao.get(id);
	}

	public void update(Lesson lesson) {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			lessonDao.update(lesson);
		}
	}

	public List<Lesson> getAll() {
		return lessonDao.findAll();
	}

	private boolean hasLessonsWithSameGroups(Lesson lesson) {
		return !lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId()).isEmpty();
	}

	private boolean hasLessonsWithSameTeacher(Lesson lesson) {
		return !lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId())
				.isEmpty();
	}

	private boolean hasRightToTeach(Lesson lesson) {
		return lesson.getTeacher().getSubjects().stream().anyMatch(s -> s.equals(lesson.getSubject()));
	}

	private boolean isEmptyAudience(Lesson lesson) {
		return lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId())
				.isEmpty();
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
