package com.basics.basics.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
    private long employeeId;
    private String employeeName;
    private String email;
    private long salary;
}
