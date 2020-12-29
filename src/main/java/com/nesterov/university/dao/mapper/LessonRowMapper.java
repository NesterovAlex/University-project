package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Lesson;

@Component
public class LessonRowMapper implements RowMapper<Lesson> {

	private GroupDao groupDao;
	private AudienceDao audienceDao;
	private SubjectDao subjectDao;
	private TeacherDao teacherDao;
	private LessonTimeDao lessonTimeDao;

	public LessonRowMapper(AudienceDao audienceDao, SubjectDao subjectDao, TeacherDao teacherDao,
			LessonTimeDao lessonTimeDao, GroupDao groupDao) {
		this.audienceDao = audienceDao;
		this.subjectDao = subjectDao;
		this.teacherDao = teacherDao;
		this.lessonTimeDao = lessonTimeDao;
		this.groupDao = groupDao;
	}

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setId(rs.getLong("id"));
		lesson.setGroups(groupDao.findByLessonId(rs.getLong("id")));
		lesson.setDate(rs.getObject("lesson_date", LocalDate.class));
		audienceDao.get(rs.getLong("audience_id")).ifPresent(lesson::setAudience);
		lessonTimeDao.get(rs.getLong("lesson_time_id")).ifPresent(lesson::setTime);
		teacherDao.get(rs.getLong("teacher_id")).ifPresent(lesson::setTeacher);
		subjectDao.get(rs.getLong("subject_id")).ifPresent(lesson::setSubject);
		return lesson;
	}
}
