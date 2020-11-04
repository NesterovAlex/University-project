package com.nesterov.university.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.jdbc.core.RowMapper;
import com.nesterov.university.dao.AudienceDao;
import com.nesterov.university.dao.GroupDao;
import com.nesterov.university.dao.LessonTimeDao;
import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.dao.TeacherDao;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;

public class LessonRowMapper implements RowMapper<Lesson>{

	private AudienceDao audienceDao;
	private SubjectDao subjectDao;
	private TeacherDao teacherDao;
	private LessonTimeDao lessonTimeDao;
	private GroupDao groupDao;
	
	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setAudience(audienceDao.get(rs.getLong("audience_id")));
		lesson.setSubject(subjectDao.getSubject(rs.getLong("subject_id")));
		lesson.setTeacher(teacherDao.getTeacher(rs.getLong("teacher_id")));
		lesson.setTime(lessonTimeDao.get(rs.getLong("lesson_time_id")));
		lesson.setDate(rs.getDate("lesson_date").toLocalDate());
		ArrayList<Group> groups = new ArrayList<>();
		groups.add(groupDao.get(rs.getLong("group_id")));
		lesson.setGroups(groups);
		return lesson;
	}

}
