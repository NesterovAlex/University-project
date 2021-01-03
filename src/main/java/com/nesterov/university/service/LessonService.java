package com.nesterov.university.service;

import static java.lang.String.format;

import java.time.DayOfWeek;
import java.util.List;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.LessonDao;
import com.nesterov.university.service.exceptions.HasLessonsWithSameGroupsException;
import com.nesterov.university.service.exceptions.HasNotEnoughtPlacesException;
import com.nesterov.university.service.exceptions.HasNotRightToTeachException;
import com.nesterov.university.service.exceptions.LessonsWithSameTeacherException;
import com.nesterov.university.service.exceptions.NotEmptyAudienceException;
import com.nesterov.university.service.exceptions.NotFoundException;
import com.nesterov.university.service.exceptions.WeekendDayException;
import com.nesterov.university.model.Lesson;

@Component
public class LessonService {

	private LessonDao lessonDao;

	public LessonService(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public void create(Lesson lesson) {
		verifyHasNotLessonsWithSameGroups(lesson);
		verifyHasNotLessonsWithSameTeacher(lesson);
		verifyHasTeacherRightToTeach(lesson);
		verifyAudienceIsEmpty(lesson);
		verifyAudienceHasEnoughPlacesIn(lesson);
		verifyIsNotWeekend(lesson);
		lessonDao.create(lesson);
	}

	public void delete(long id) {
		if (!lessonDao.get(id).isPresent()) {
			String message = format("Lesson with id = '%s' not found", id);
			throw new NotFoundException(message);
		}

		lessonDao.delete(id);
	}

	public Lesson get(long id) {
		String message = format("Lesson with id = '%s' not found", id);
		return lessonDao.get(id).orElseThrow(() -> new NotFoundException(message));
	}

	public void update(Lesson lesson) {
		verifyHasNotLessonsWithSameGroups(lesson);
		verifyHasNotLessonsWithSameTeacher(lesson);
		verifyHasTeacherRightToTeach(lesson);
		verifyAudienceIsEmpty(lesson);
		verifyAudienceHasEnoughPlacesIn(lesson);
		verifyIsNotWeekend(lesson);
		lessonDao.update(lesson);
	}

	public List<Lesson> getAll() {
		return lessonDao.findAll();
	}

	private void verifyHasNotLessonsWithSameGroups(Lesson lesson) {
		if (!lessonDao.findByDateAndGroups(lesson.getDate(), lesson.getTime().getId()).isEmpty()) {
			throw new HasLessonsWithSameGroupsException(
					"The groups in this lesson have other lessons at this time and date");
		}
	}

	private void verifyHasNotLessonsWithSameTeacher(Lesson lesson) {
		if (!lessonDao.findByDateAndTeacher(lesson.getDate(), lesson.getTime().getId(), lesson.getTeacher().getId())
				.isEmpty()) {
			String message = format("Teacher with id = '%s' has other lessons in this time and date",
					lesson.getTeacher().getId());
			throw new LessonsWithSameTeacherException(message);
		}
	}

	private void verifyHasTeacherRightToTeach(Lesson lesson) {
		if (lesson.getTeacher().getSubjects().stream().noneMatch(s -> s.equals(lesson.getSubject()))) {
			String message = String.format("Teacher with id = '%s' has not right to teach this lesson",
					lesson.getTeacher().getId());
			throw new HasNotRightToTeachException(message);
		}
	}

	private void verifyAudienceIsEmpty(Lesson lesson) {
		if (!lessonDao.findByDateAndAudience(lesson.getDate(), lesson.getTime().getId(), lesson.getAudience().getId())
				.isEmpty()) {
			String message = format("Audience with id='%s' not empty", lesson.getAudience().getId());
			throw new NotEmptyAudienceException(message);
		}
	}

	private void verifyAudienceHasEnoughPlacesIn(Lesson lesson) {
		if (countStudentsOfLesson(lesson) > lesson.getAudience().getCapacity()) {
			String message = String.format("Has not enought places in audience with roomNumber = '%s'",
					lesson.getAudience().getRoomNumber());
			throw new HasNotEnoughtPlacesException(message);
		}
	}

	private long countStudentsOfLesson(Lesson lesson) {
		return lesson.getGroups().stream().mapToLong(g -> g.getStudents().size()).sum();
	}

	private void verifyIsNotWeekend(Lesson lesson) {
		if (lesson.getDate().getDayOfWeek() == DayOfWeek.SATURDAY
				|| lesson.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
			throw new WeekendDayException("This is weekend");
		}
	}
}
