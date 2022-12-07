package com.premukkoji.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/* if this extension is used then no need of AutoCloseable */
//@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService studentService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        studentService = new StudentService(studentRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetStudents() {
        // when
        studentService.getStudents();

        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddNewStudent() {
        // given
        Student student = new Student(
                "Prem Ukkoji",
                "premukkoji@gmail.com",
                LocalDate.of(1997, 2,17)
        );

        // when
        studentService.addNewStudent(student);

        // then
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(argumentCaptor.capture());

        Student capturedStudent = argumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void shouldThrowWhenEmailExists() {
        // given
        Student student = new Student(
                "Prem Ukkoji",
                "premukkoji@gmail.com",
                LocalDate.of(1997, 2,17)
        );

        given(studentRepository.isStudentPresentByEmail(anyString()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> studentService.addNewStudent(student))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldDeleteStudentWhenPresent(){
        // given
        Long studentId = 1L;
        given(studentRepository.existsById(studentId))
                .willReturn(true);

        // when
        studentService.deleteStudent(studentId);

        // then
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void shouldThrowWhenStudentIdDoesNotExists() {
        // given
        Long studentId = 1L;
        given(studentRepository.existsById(studentId))
                .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> studentService.deleteStudent(studentId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("student with id=" + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(any());
    }
}