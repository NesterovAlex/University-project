package com.nesterov.university.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.nesterov.university.dao.mapper.GroupRowMapper;
import com.nesterov.university.dao.mapper.LessonRowMapper;
import com.nesterov.university.model.Group;
import com.nesterov.university.model.Lesson;

@Component
public class LessonDao {

	private static final String SELECT_ALL_GROUPS_BY_LESSON = "SELECT * FROM groups LEFT JOIN lessons_groups ON lessons_groups.group_id = groups.id WHERE lesson_id = ?";
	private static final String DELETE_FROM_LESSONS_GROUPS = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id = ?";
	private static final String INSERT_INTO_LESSONS_GROUPS = "INSERT INTO lessons_groups SELECT ?, ? FROM DUAL WHERE NOT EXISTS (SELECT FROM lessons_groups WHERE lesson_id = ? AND group_id = ?);";
	private static final String SELECT_BY_ID = "SELECT * FROM lessons WHERE id = ?";
	private static final String SELECT = "SELECT * FROM lessons";
	private static final String INSERT = "INSERT INTO lessons (audience_id, subject_id, teacher_id, lesson_time_id, lesson_date) values (?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE lessons SET audience_id = ?, subject_id = ?, teacher_id = ?, lesson_time_id = ?, lesson_date = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM lessons WHERE id = ?";

	private LessonRowMapper lessonRowMapper;
	private GroupRowMapper groupRowMapper;
	private JdbcTemplate jdbcTemplate;

	public LessonDao(JdbcTemplate template, @Lazy LessonRowMapper lessonRowMapper, GroupRowMapper groupRowMapper) {
		this.jdbcTemplate = template;
		this.lessonRowMapper = lessonRowMapper;
		this.groupRowMapper = groupRowMapper;
	}

	@Transactional
	public void create(Lesson lesson) {
		final KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT, new String[] { "id" });
			statement.setLong(1, lesson.getAudience().getId());
			statement.setLong(2, lesson.getSubject().getId());
			statement.setLong(3, lesson.getTeacher().getId());
			statement.setLong(4, lesson.getTime().getId());
			statement.setObject(5, lesson.getDate());
			return statement;
		}, holder);
		long id = holder.getKey().longValue();
		lesson.setId(id);
		lesson.getGroups().forEach(g -> jdbcTemplate.update(INSERT_INTO_LESSONS_GROUPS, id, g.getId(), id, g.getId()));
	}

	public Lesson get(long id) {
		return jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[] { id }, lessonRowMapper);
	}

	@Transactional
	public void delete(long id) {
		jdbcTemplate.update(DELETE, id);
	}

	@Transactional
	public void update(Lesson lesson) {
		jdbcTemplate.update(UPDATE, lesson.getAudience().getId(), lesson.getSubject().getId(),
				lesson.getTeacher().getId(), lesson.getTime().getId(), lesson.getDate(), lesson.getId());
		List<Group> groups = jdbcTemplate.query(SELECT_ALL_GROUPS_BY_LESSON, groupRowMapper, lesson.getId());
		groups.removeAll(lesson.getGroups());
		groups.forEach(g -> jdbcTemplate.update(DELETE_FROM_LESSONS_GROUPS, lesson.getId(), g.getId()));
		lesson.getGroups().forEach(g -> jdbcTemplate.update(INSERT_INTO_LESSONS_GROUPS, lesson.getId(), g.getId(),
				lesson.getId(), g.getId()));
	}

	public List<Lesson> getAll() {
		return jdbcTemplate.query(SELECT, lessonRowMapper);
	}
}
