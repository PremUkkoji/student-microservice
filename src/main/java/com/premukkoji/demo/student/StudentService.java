package com.premukkoji.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Cacheable(value = "students", key = "#studentId")
    public Optional<Student> getStudent(Long studentId) {
        log.info("Fetching student with " + studentId + " from DB");
        return studentRepository.findById(studentId);
    }

    public void addNewStudent(Student student) {
        Boolean exists = studentRepository.isStudentPresentByEmail(student.getEmail());

        if (exists) {
            throw new IllegalStateException("email taken");
        }

        studentRepository.save(student);
    }

    @CacheEvict(value = "students", key = "#studentId", allEntries = true)
    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);

        if (!exists)
            throw new IllegalStateException("student with id=" + studentId + " does not exists");

        studentRepository.deleteById(studentId);
    }

    @CachePut(value = "students", key = "#studentId")
    @Transactional
    public Student updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    throw new IllegalStateException("student with id=" + studentId + " does not exists");
                });

        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {
            Optional<Student> studentByEmail = studentRepository.findStudentByEmail(email);

            if (studentByEmail.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }

        return student;
    }
}
