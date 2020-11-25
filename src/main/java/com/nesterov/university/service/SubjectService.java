package com.nesterov.university.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nesterov.university.dao.SubjectDao;
import com.nesterov.university.model.Subject;
import com.nesterov.university.model.Teacher;

@Component
public class SubjectService {

	private SubjectDao subjectDao;

	public SubjectService(SubjectDao subjectDao) {
		this.subjectDao = subjectDao;
	}
	
	public Subject createSubject(Subject subject) {
		subjectDao.create(subject);
		return subject;
	}
	
	public long deleteSubject(Subject subject) {
		subjectDao.delete(subject.getId());
		return subject.getId();
	}
	
	public Subject getSubject(Subject subject) {
		return subjectDao.get(subject.getId());
	}
	
	public void updateSubject(Subject subject) {
		subjectDao.update(subject);
	}
	
	public List<Subject> findAll(){
		return subjectDao.getAll();
	}
	
	public List<Subject> findByTeacherId(long id){
		return subjectDao.findByTeacherId(id);
	}
	
	public void addTeacher(Subject subject, Teacher teacher) {
		subject.getTeachers().add(teacher);
		subjectDao.update(subject);
	}
	
	public void removeTeacher(Subject subject, Teacher teacher) {
		subject.getTeachers().remove(teacher);
		subjectDao.update(subject);
	}
}
