package com.basics.basics.services.impl;

import com.basics.basics.TestContainerConfiguration;
import com.basics.basics.dto.EmployeeDto;
import com.basics.basics.entities.Employee;
import com.basics.basics.exceptions.ResourceNotFoundException;
import com.basics.basics.repositories.EmployeeRepository;
import com.basics.basics.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp() {
        mockEmployee = new Employee(1L, "Anand", "anand56@gmail.com",100L);

        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDto.class);

    }


    @Test
    void testGetEmployeeById_whenEmployeeIsPresent_thenReturnEmployeeDto() {

        //Given
        Long employeeId = mockEmployee.getEmployeeId();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee)); // stubbing : define behaviour also like what should method should return

        //When
        EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);

        //Then
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmployeeId()).isEqualTo(employeeId);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());

        verify(employeeRepository).findById(employeeId); // to verify whether the findById method from employeeRepository mock
//        verify(employeeRepository).save(mockEmployee);
    }

    @Test
    void testGetEmployeeById_whenEmployeeIsNotPresent_thenReturnEmployeeDto() {
        // given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when and then combined

        assertThatThrownBy(()-> employeeService.getEmployeeById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Employee with id: 1 not found");



//        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(mockEmployee.getEmployeeId()));
    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNewEmployee(){

        //assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        //act
        EmployeeDto newEmployee = employeeService.createEmployee(mockEmployeeDto);

        //assert
        assertThat(newEmployee).isNotNull();
        assertThat(newEmployee.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        verify(employeeRepository).save(any(Employee.class));

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee).isNotNull();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    @DisplayName("TestEmployeeAlreadyExists")
    void testCreateNewEmployee_whenEmployeeAlreadyPresent_thenThrowRuntimeException(){
        // given
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of(mockEmployee));

        // act and assert
        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(mockEmployeeDto));
    }

    @Test
    void testUpdateEmployee_whenEmployeeExistsAndNotUpdatingEmail_thenReturnUpdatedEmployee(){
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setEmployeeName("New Name");
        mockEmployeeDto.setSalary(50L);

        Employee temp = modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any())).thenReturn(temp);


        // act
        EmployeeDto updatedEmployee = employeeService.updateEmployee(mockEmployeeDto);

        // assert
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        assertThat(updatedEmployee).isNotEqualTo(mockEmployee);
    }

    @Test
    void testUpdateEmployee_whenEmployeeExistsButUpdatesEmail_thenThrowRuntimeException(){
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setEmail("changedEmail@gmail.com");
        mockEmployeeDto.setEmployeeName("New Name");

        // act and assert
        assertThatThrownBy(()->employeeService.updateEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employees cannot be updated");
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowRuntimeException(){
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act and assert
        assertThatThrownBy(()->employeeService.updateEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee with id: " + mockEmployeeDto.getEmployeeId() +" not found");
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(mockEmployee));

        // act
        employeeService.deleteEmployee(mockEmployee.getEmployeeId());


        // assert
        verify(employeeRepository, times(1)).delete(mockEmployee);
        verify(employeeRepository).delete(mockEmployee);
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowRuntimeException(){
        // assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // act and assert
        assertThatThrownBy(()-> employeeService.deleteEmployee(mockEmployee.getEmployeeId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee with id: " + mockEmployee.getEmployeeId() + " not found");
    }

}