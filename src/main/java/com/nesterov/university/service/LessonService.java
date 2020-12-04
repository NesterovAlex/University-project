package com.nesterov.university.service;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.containsAny;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
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
		if (!hasLessonsWithSameGroups(lesson) && !hasLessonsWithSameTeacher(lesson) && hasTheRightToTeach(lesson)
				&& !isEmptyAudience(lesson) && hasEnoughPlaces(lesson)) {
			lessonDao.create(lesson);
		}
	}

	public void delete(long id) {
		lessonDao.delete(id);
	}

	public Lesson get(long id) {
		return lessonDao.get(id);
	}

	public void update(Lesson subject) {
		lessonDao.update(subject);
	}

	public List<Lesson> getAll() {
		return lessonDao.findAll();
	}

	private boolean hasLessonsWithSameGroups(Lesson lesson) {
		boolean hasLesons = true;
		if (findLessonsByLessonTime(lesson).filter(l -> containsAny(l.getGroups(), lesson.getGroups()))
				.collect(toList()).isEmpty()) {
			hasLesons = false;
		}
		return hasLesons;
	}

	private boolean hasLessonsWithSameTeacher(Lesson lesson) {
		boolean hasLessons = true;
		if (findLessonsByLessonTime(lesson).filter(l -> l.getTeacher().equals(lesson.getTeacher())).collect(toList())
				.isEmpty()) {
			hasLessons = false;
		}
		return hasLessons;
	}

	private boolean hasTheRightToTeach(Lesson lesson) {
		boolean hasRight = true;
		if (lesson.getTeacher().getSubjects().stream().filter(s -> s.equals(lesson.getSubject())).collect(toList())
				.isEmpty()) {
			hasRight = false;
		}
		return hasRight;
	}

	private boolean isEmptyAudience(Lesson lesson) {
		boolean isEmpty = true;
		if (findLessonsByLessonTime(lesson).allMatch(l -> l.getAudience().equals(lesson.getAudience()))) {
			isEmpty = false;
		}
		return isEmpty;
	}

	private boolean hasEnoughPlaces(Lesson lesson) {
		boolean hasEnoughPlaces = true;
		if (countStudentsOfLesson(lesson) > lesson.getAudience().getCapacity()) {
			hasEnoughPlaces = false;
		}
		return hasEnoughPlaces;
	}

	private Stream<Lesson> findLessonsByLessonTime(Lesson lesson) {
		return lessonDao.findByDate(lesson.getDate()).stream().filter(l -> l.getTime().equals(lesson.getTime()));
	}

	private long countStudentsOfLesson(Lesson lesson) {
		AtomicInteger count = new AtomicInteger(0);
		lesson.getGroups().forEach(g -> count.addAndGet(g.getStudents().size()));
		return count.longValue();
	}
}
