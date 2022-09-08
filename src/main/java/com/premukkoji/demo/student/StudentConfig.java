package com.premukkoji.demo.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student prem = new Student(
                    "Prem",
                    "premukkoji@gmail.com",
                    LocalDate.of(1997, Month.FEBRUARY, 17));

            Student john = new Student(
                    "John",
                    "John@gmail.com",
                    LocalDate.of(2000, Month.JANUARY, 1));

            repository.saveAll(List.of(prem, john));
        };
    }

}
