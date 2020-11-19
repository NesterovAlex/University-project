package com.nesterov.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.nesterov.university.model.LessonTime;

@Component
public class LessonTimeRowMapper implements RowMapper<LessonTime>{

	@Override
	public LessonTime mapRow(ResultSet rs, int rowNum) throws SQLException {
		LessonTime lessonTime = new LessonTime();
		lessonTime.setId(rs.getLong("id"));
		lessonTime.setOrderNumber(rs.getInt("order_number"));
		lessonTime.setStart(rs.getObject("start_lesson", LocalTime.class));
		lessonTime.setEnd(rs.getObject("end_lesson", LocalTime.class));
		return lessonTime;
	}
}
