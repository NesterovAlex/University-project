package com.nesterov.university.service;

import java.time.DayOfWeek;
import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.dao.exceptions.EntityNotFoundException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotExistException;
import com.nesterov.university.dao.exceptions.QueryNotExecuteException;
import com.nesterov.university.model.Lesson;

@Component
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) throws NotCreateException, EntityNotFoundException, QueryNotExecuteException {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public void delete(long id) throws NotExistException {
		lessonDao.delete(id);
	}

	public Lesson get(long id) throws EntityNotFoundException, QueryNotExecuteException {
		return lessonDao.get(id);
	}

	public void update(Lesson lesson) throws EntityNotFoundException, QueryNotExecuteException, NotCreateException {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			lessonDao.update(lesson);
		}
	}

	public List<Lesson> getAll() throws EntityNotFoundException, QueryNotExecuteException {
		return lessonDao.findAll();
	}

	private boolean hasLessonsWithSameGroups(Lesson lesson) throws EntityNotFoundException, QueryNotExecuteException {
		return !lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId()).isEmpty();
	}

	private boolean hasLessonsWithSameTeacher(Lesson lesson) throws EntityNotFoundException, QueryNotExecuteException {
		return !lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId())
				.isEmpty();
	}

	private boolean hasRightToTeach(Lesson lesson) {
		return lesson.getTeacher().getSubjects().stream().anyMatch(s -> s.equals(lesson.getSubject()));
	}

	private boolean isEmptyAudience(Lesson lesson) throws EntityNotFoundException, QueryNotExecuteException {
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
