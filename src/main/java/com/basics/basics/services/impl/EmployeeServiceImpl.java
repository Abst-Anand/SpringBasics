package com.basics.basics.services.impl;

import com.basics.basics.dto.EmployeeDto;
import com.basics.basics.entities.Employee;
import com.basics.basics.exceptions.ResourceNotFoundException;
import com.basics.basics.repositories.EmployeeRepository;
import com.basics.basics.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    private final String CACHE_NAME = "employee";

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "#id") // key = "{#id, #otherParam}
    public EmployeeDto getEmployeeById(long id) {
        log.info("Fetching employee by id: {}", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(()->{
            log.error("Employee with id: {} not found", id);
            return new ResourceNotFoundException("Employee with id: " + id + " not found");
        });
        log.info("Successfully fetched employee by id: {}", id);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee with email: {}", employeeDto.getEmail());
        if(!employeeRepository.findByEmail(employeeDto.getEmail()).isEmpty()){
            log.error("Employee with email: {} already exists", employeeDto.getEmail());
            throw new RuntimeException("Employee with email: " + employeeDto.getEmail() + " already exists");
        }
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee = employeeRepository.save(employee);
        log.info("Successfully created employee with id: {}", employee.getEmployeeId());
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @CachePut(cacheNames = CACHE_NAME, key = "#employeeDto.getEmployeeId()")
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        log.info("Updating employee with id: {}", employeeDto.getEmployeeId());
        Employee employee = employeeRepository.findById(employeeDto.getEmployeeId()).orElseThrow(()->{
            log.error("Employee with id: {} not found", employeeDto.getEmployeeId());
            throw new RuntimeException("Employee with id: " + employeeDto.getEmployeeId() + " not found");
        });

        if(!employee.getEmail().equals(employeeDto.getEmail())){
            log.error("Attempted to update email for employee with id: {}", employeeDto.getEmployeeId());
            throw new RuntimeException("The email of the employees cannot be updated");
        }
        Employee updatedEmployee = modelMapper.map(employeeDto, Employee.class);
        employee = employeeRepository.save(updatedEmployee);
        log.info("Successfully updated employee with id: {}", employee.getEmployeeId());
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
    public void deleteEmployee(long id) {
        log.info("Deleting employee with id: {}", id);

        Employee employee = employeeRepository.findById(id).orElseThrow(()->{
            log.error("Employee with id: {} not found", id);
            throw new RuntimeException("Employee with id: " + id + " not found");
        });
        employeeRepository.delete(employee);
        log.info("Successfully deleted employee with id: {}", id);
    }
}
