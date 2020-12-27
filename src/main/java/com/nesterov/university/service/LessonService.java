package com.nesterov.university.service;

import static java.lang.String.format;

import java.time.DayOfWeek;
import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.dao.exceptions.HasLessonsWithSameGroupsException;
import com.nesterov.university.dao.exceptions.HasNotEnoughtPlacesException;
import com.nesterov.university.dao.exceptions.HasNotRightToTeachException;
import com.nesterov.university.dao.exceptions.LessonsWithSameTeacherException;
import com.nesterov.university.dao.exceptions.NotCreateException;
import com.nesterov.university.dao.exceptions.NotDeleteException;
import com.nesterov.university.dao.exceptions.NotEmptyAudienceException;
import com.nesterov.university.dao.exceptions.NotFoundEntitiesException;
import com.nesterov.university.dao.exceptions.NotPresentEntityException;
import com.nesterov.university.dao.exceptions.WeekendDayException;
import com.nesterov.university.model.Lesson;

@Component
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson)
			throws WeekendDayException, NotCreateException, HasNotEnoughtPlacesException, NotEmptyAudienceException,
			HasNotRightToTeachException, LessonsWithSameTeacherException, HasLessonsWithSameGroupsException {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public void delete(long id) throws NotDeleteException {
		lessonDao.delete(id);
	}

	public Lesson get(long id) throws NotPresentEntityException {
		String message = format("Lesson with id = '%s' not found", id);
		return lessonDao.get(id).orElseThrow(() -> new NotPresentEntityException(message));
	}

	public void update(Lesson lesson)
			throws WeekendDayException, HasNotEnoughtPlacesException, NotEmptyAudienceException,
			HasNotRightToTeachException, LessonsWithSameTeacherException, HasLessonsWithSameGroupsException {
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasRightToTeach(lesson)
				&& isEmptyAudience(lesson) && hasEnoughPlaces(lesson) && !isWeekend(lesson)) {
			lessonDao.update(lesson);
		}
	}

	public List<Lesson> getAll() throws NotFoundEntitiesException {
		List<Lesson> lessons = lessonDao.findAll();
		if (lessons.isEmpty()) {
			throw new NotFoundEntitiesException("Not found lessons");
		}
		return lessons;
	}

	private boolean hasLessonsWithSameGroups(Lesson lesson) throws HasLessonsWithSameGroupsException {
		List<Lesson> lessons = lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId());
		if (!lessons.isEmpty()) {
			throw new HasLessonsWithSameGroupsException(
					"The groups in this lesson have other lessons at this time and date");
		}
		return !lessons.isEmpty();
	}

	private boolean hasLessonsWithSameTeacher(Lesson lesson) throws LessonsWithSameTeacherException {
		List<Lesson> lessons = lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(),
				lesson.getTeacher().getId());
		if (!lessons.isEmpty()) {
			String message = format("Teacher with id = '%s' has other lessons in this time and date",
					lesson.getTeacher().getId());
			throw new LessonsWithSameTeacherException(message);
		}
		return !lessons.isEmpty();
	}

	private boolean hasRightToTeach(Lesson lesson) throws HasNotRightToTeachException {
		if (lesson.getTeacher().getSubjects().stream().noneMatch(s -> s.equals(lesson.getSubject()))) {
			String message = String.format("Teacher with id = '%s' has not right to teach this lesson",
					lesson.getTeacher().getId());
			throw new HasNotRightToTeachException(message);
		}
		return lesson.getTeacher().getSubjects().stream().anyMatch(s -> s.equals(lesson.getSubject()));
	}

	private boolean isEmptyAudience(Lesson lesson) throws NotEmptyAudienceException {
		List<Lesson> lessons = lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(),
				lesson.getAudience().getId());
		if (!lessons.isEmpty()) {
			String message = format("Audience with id='%s' not empty", lesson.getAudience().getId());
			throw new NotEmptyAudienceException(message);
		}
		return lessons.isEmpty();
	}

	private boolean hasEnoughPlaces(Lesson lesson) throws HasNotEnoughtPlacesException {
		long countStudentsOfLesson = countStudentsOfLesson(lesson);
		if (countStudentsOfLesson > lesson.getAudience().getCapacity()) {
			String message = String.format("Has not enought places in audience with roomNumber = '%s'",
					lesson.getAudience().getRoomNumber());
			throw new HasNotEnoughtPlacesException(message);
		}
		return countStudentsOfLesson(lesson) <= lesson.getAudience().getCapacity();
	}

	private long countStudentsOfLesson(Lesson lesson) {
		return lesson.getGroups().stream().mapToLong(g -> g.getStudents().size()).sum();
	}

	private boolean isWeekend(Lesson lesson) throws WeekendDayException {
		boolean isWeekend = lesson.getDate().getDayOfWeek() == DayOfWeek.SATURDAY
				|| lesson.getDate().getDayOfWeek() == DayOfWeek.SUNDAY;
		if (isWeekend) {
			throw new WeekendDayException("This is weekend");
		}
		return isWeekend;
	}
}
