package com.basics.basics.services;

import com.basics.basics.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto getEmployeeById(long id);
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(EmployeeDto employeeDto);
    void deleteEmployee(long id);
}
