package com.basics.basics.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto implements Serializable {
    private long employeeId;
    private String employeeName;
    private String email;
    private long salary;
}
