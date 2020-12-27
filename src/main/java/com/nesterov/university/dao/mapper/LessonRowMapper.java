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
import com.nesterov.university.model.Subject;

@Component
public class LessonRowMapper implements RowMapper<Lesson> {

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

	private GroupDao groupDao;

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setId(rs.getLong("id"));
		lesson.setAudience(audienceDao.get(rs.getLong("audience_id")).orElse(null));
		lesson.setGroups(groupDao.findByLessonId(rs.getLong("id")));
		lesson.setTime(lessonTimeDao.get(rs.getLong("lesson_time_id")).orElse(null));
		lesson.setTeacher(teacherDao.get(rs.getLong("teacher_id")).orElse(null));
		lesson.setSubject(subjectDao.get(rs.getLong("subject_id")).orElse(new Subject()));
		lesson.setDate(rs.getObject("lesson_date", LocalDate.class));
		return lesson;
	}
}
