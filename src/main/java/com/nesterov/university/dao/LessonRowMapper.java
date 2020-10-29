package com.nesterov.university.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.nesterov.university.model.Audience;
import com.nesterov.university.model.Lesson;
import com.nesterov.university.model.LessonTime;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

public class LessonRowMapper implements RowMapper<Lesson>{

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		Audience audience = new Audience();
		audience.setId(rs.getLong("audience"));
		Subject subject = new Subject();
		subject.setId(rs.getLong("subject"));
		Teacher teacher = new Teacher();
		teacher.setId(rs.getLong("teacher"));
		LessonTime time = new LessonTime();
		lesson.setId(rs.getLong("lesson_time"));
		LocalDate date = rs.getDate("lesson_date").toLocalDate();
		lesson.setAudience(audience);
		lesson.setSubject(subject);
		lesson.setTeacher(teacher);
		lesson.setTime(time);
		lesson.setDate(date);
		return lesson;
	}

}
