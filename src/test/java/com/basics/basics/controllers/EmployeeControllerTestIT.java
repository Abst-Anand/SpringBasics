package com.basics.basics.controllers;

import com.basics.basics.TestContainerConfiguration;
import com.basics.basics.dto.EmployeeDto;
import com.basics.basics.entities.Employee;
import com.basics.basics.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class EmployeeControllerTestIT extends AbstractIntegrationTestClass{

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;
    private EmployeeDto testEmployeeDto;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee(1L,"Anand","araaz56@gmail.com", 100L);
        testEmployeeDto = new EmployeeDto(1L,"Anand","araaz56@gmail.com", 100L);

        employeeRepository.deleteAll();
    }


    @Test
    void testGetEmployeeById_success() {

        Employee savedEmployee = employeeRepository.save(testEmployee);

        webClient.get()
                .uri("/employees/{id}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.employeeId").isEqualTo(savedEmployee.getEmployeeId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
//                .value(emp -> {
//                    assertThat(emp.getEmployeeId()).isEqualTo(testEmployee.getEmployeeId());
//                    assertThat(emp.getEmployeeName()).isEqualTo(testEmployee.getEmployeeName());
//
//                });
    }

    @Test
    void testGetEmployeeById_failure() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        savedEmployee.setEmployeeId(2L); // change to wrong id to expect failure
        webClient.get()
                .uri("/employees/{id}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(EmployeeDto.class);
    }

    @Test
    void testCreateEmployee_whenEmployeeAlreadyExists_thenThrowException() {
        //save an employee directly first
        Employee savedEmployee = employeeRepository.save(testEmployee);

        // now try to save the save employee using controller
        webClient.post()
                .uri("/employees")
                .bodyValue(testEmployee)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateEmployee_whenEmployeeDoesNotExist_thenCreateNewEmployee() {

        webClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.employeeName").isEqualTo(testEmployeeDto.getEmployeeName());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowException() {

        testEmployee.setEmployeeId(-2L);

        webClient.put()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptToUpdateEmail_thenThrowException() {

        Employee savedEmployee = employeeRepository.save(testEmployee);
        testEmployee.setEmail("diiff@gmail.com");

        webClient.put()
                .uri("/employees")
                .bodyValue(testEmployee)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsValid_thenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        savedEmployee.setSalary(10L);
        savedEmployee.setEmployeeName("New Name");

        webClient.put()
                .uri("/employees")
                .bodyValue(savedEmployee)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.employeeName").isEqualTo(savedEmployee.getEmployeeName())
                .jsonPath("$.salary").isEqualTo(savedEmployee.getSalary());
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowException() {
        webClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webClient.delete()
                .uri("/employees/{id}", savedEmployee.getEmployeeId())
                .exchange()
                .expectStatus().isOk();

        // again try to delete the same employee
        webClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

}