package com.premukkoji.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void isStudentPresentByEmail() {
        // given
        String email = "premukkoji@gmail.com";
        Student student = new Student(
                "Prem Ukkoji",
                email,
                LocalDate.of(1997, 2, 17)
        );

        // when
        studentRepository.save(student);

        // then
        Boolean expected = studentRepository.isStudentPresentByEmail(email);
        assertThat(expected).isTrue();
    }

    @Test
    void isStudentNotPresentByEmail() {
        // given
        String email = "premukkoji@gmail.com";

        // when
        Boolean expected = studentRepository.isStudentPresentByEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}