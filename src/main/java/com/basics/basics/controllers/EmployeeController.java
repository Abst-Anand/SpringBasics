package com.basics.basics.controllers;

import com.basics.basics.dto.EmployeeDto;
import com.basics.basics.entities.Employee;
import com.basics.basics.services.EmployeeService;
import com.basics.basics.services.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable long id) {
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto newEmployeeDto = employeeService.createEmployee(employeeDto);
        return ResponseEntity.ok(newEmployeeDto);
    }

    @PutMapping
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(employeeDto);
        return ResponseEntity.ok(updatedEmployeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable long id) {
        EmployeeDto deletedEmployee = employeeService.getEmployeeById(id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(deletedEmployee);
    }

}