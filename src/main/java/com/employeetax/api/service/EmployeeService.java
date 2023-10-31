package com.employeetax.api.service;

import com.employeetax.api.entity.Employee;
import com.employeetax.api.exception.EmployeeNotFoundException;
import com.employeetax.api.exception.InvalidDataException;
import com.employeetax.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Employee saveEmployee(Employee employee) {
        // Add validation and business logic here
        if (employee.getEmployeeID() != null) {
            throw new InvalidDataException("New employee cannot have an ID.");
        }
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            throw new EmployeeNotFoundException(id);
        }
        return employeeOptional;
    }

}

