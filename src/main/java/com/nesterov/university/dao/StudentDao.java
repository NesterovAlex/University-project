package com.nesterov.university.dao;

import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.nesterov.university.dao.mapper.StudentRowMapper;
import com.nesterov.university.model.Student;

@Component
public class StudentDao {

    private static final Logger log = LoggerFactory.getLogger(StudentDao.class);

    private static final String SELECT_BY_GROUP = "SELECT * FROM students WHERE group_id = ?";
    private static final String SELECT_BY_ID = "SELECT *  FROM students WHERE id = ?";
    private static final String SELECT_BY_EMAIL = "SELECT *  FROM students WHERE email = ?";
    private static final String SELECT_BY_PHONE = "SELECT *  FROM students WHERE phone = ?";
    private static final String SELECT_BY_ADDRESS = "SELECT *  FROM students WHERE address = ?";
    private static final String SELECT = "SELECT * FROM students";
    private static final String INSERT = "INSERT INTO students (group_id, first_name, last_name, birth_date, address, email, phone, gender, faculty, course) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE students SET group_id = ?, first_name = ?, last_name = ?, birth_date = ?, address = ?, email = ?, phone = ?, gender = ?, faculty = ?, course = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM students WHERE id = ?";

    private JdbcTemplate jdbcTemplate;
    private StudentRowMapper studentRowMapper;

    public StudentDao(JdbcTemplate template, StudentRowMapper studentRowMapper) {
        this.jdbcTemplate = template;
        this.studentRowMapper = studentRowMapper;
    }

    public void create(Student student) {
        log.debug("Create {}", student);
        final KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT, new String[]{"id"});
            statement.setLong(1, student.getGroupId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.setObject(4, student.getBithDate());
            statement.setString(5, student.getAddress());
            statement.setString(6, student.getEmail());
            statement.setString(7, student.getPhone());
            statement.setString(8, student.getGender().name());
            statement.setString(9, student.getFaculty());
            statement.setString(10, student.getCourse());
            return statement;
        }, holder);
        student.setId(holder.getKey().longValue());
    }

    public Optional<Student> get(long id) {
        log.debug("Get student by id={}", id);
        try {
            return of(jdbcTemplate.queryForObject(SELECT_BY_ID, new Object[]{id}, studentRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    public void delete(long id) {
        log.debug("Delete student by id = {}", id);
        jdbcTemplate.update(DELETE, id);
    }

    public void update(Student student) {
        log.debug("Update student {}", student);
        jdbcTemplate.update(UPDATE, student.getGroupId(), student.getFirstName(), student.getLastName(), student.getBithDate(),
                student.getAddress(), student.getEmail(), student.getPhone(), student.getGender().name(),
                student.getFaculty(), student.getCourse(), student.getId());
    }

    public List<Student> findAll() {
        log.debug("Get all students");
        return jdbcTemplate.query(SELECT, studentRowMapper);
    }

    public List<Student> findByGroupId(long id) {
        log.debug("Get students by groupId={}", id);
        return jdbcTemplate.query(SELECT_BY_GROUP, new Object[]{id}, studentRowMapper);
    }

    public Optional<Student> findByEmail(String email) {
        log.debug("Find student by email={}", email);
        try {
            return of(jdbcTemplate.queryForObject(SELECT_BY_EMAIL, new Object[]{email}, studentRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    public Optional<Student> findByPhone(String phone) {
        log.debug("Find student by phone={}", phone);
        try {
            return of(jdbcTemplate.queryForObject(SELECT_BY_PHONE, new Object[]{phone}, studentRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }

    public Optional<Student> findByAddress(String address) {
        log.debug("Find student by address={}", address);
        try {
            return of(jdbcTemplate.queryForObject(SELECT_BY_ADDRESS, new Object[]{address}, studentRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return empty();
        }
    }
}
