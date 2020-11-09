package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
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

	private AudienceDao audienceDao;
	private SubjectDao subjectDao;
	private TeacherDao teacherDao;
	private LessonTimeDao lessonTimeDao;
	private GroupDao groupDao;

	public LessonRowMapper() {
	}

	public LessonRowMapper(JdbcTemplate template) {
		this.audienceDao = new AudienceDao(template);
		this.subjectDao = new SubjectDao(template);
		this.teacherDao = new TeacherDao(template);
		this.lessonTimeDao = new LessonTimeDao(template);
		this.groupDao = new GroupDao(template);
	}

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setAudience(audienceDao.get(rs.getLong("audience_id")));
		lesson.setSubject(subjectDao.getSubject(rs.getLong("subject_id")));
		lesson.setTeacher(teacherDao.get(rs.getLong("teacher_id")));
		lesson.setTime(lessonTimeDao.get(rs.getLong("lesson_time_id")));
		lesson.setDate(rs.getDate("lesson_date"));
		lesson.setGroups(groupDao.getAllByLesson(rs.getLong("id")));
		return lesson;
	}
}
