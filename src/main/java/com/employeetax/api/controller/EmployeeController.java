package com.employeetax.api.controller;

import com.employeetax.api.entity.Employee;
import com.employeetax.api.exception.EmployeeNotFoundException;
import com.employeetax.api.exception.InvalidDataException;
import com.employeetax.api.model.TaxInfo;
import com.employeetax.api.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    // http://localhost:8080/employees/add
    @PostMapping("/add")
    public ResponseEntity<String> addEmployee(@Valid @RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return ResponseEntity.ok("Employee added with ID: " + savedEmployee.getEmployeeID());
        } catch (InvalidDataException e) {
            throw e;
        }
    }


    // http://localhost:8080/employees/tax/{employeeID}
    @GetMapping("/tax/{employeeID}")
    public ResponseEntity<TaxInfo> calculateTax(@PathVariable Long employeeID) {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(employeeID);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            double yearlySalary = calculateYearlySalary(employee);

            double taxAmount = calculateTaxAmount(yearlySalary);
            double cessAmount = calculateCessAmount(yearlySalary);

            TaxInfo taxInfo = new TaxInfo();
            taxInfo.setEmployeeID(employee.getEmployeeID());
            taxInfo.setFirstName(employee.getFirstName());
            taxInfo.setLastName(employee.getLastName());
            taxInfo.setYearlySalary(yearlySalary);
            taxInfo.setTaxAmount(taxAmount);
            taxInfo.setCessAmount(cessAmount);

            return ResponseEntity.ok(taxInfo);
        } else {
            throw new EmployeeNotFoundException(employeeID);
        }
    }

    private double calculateYearlySalary(Employee employee) {

        double monthlySalary = employee.getSalary();
        double daysInYear = 365.0;


        int daysWorked = (int) daysInYear;

        double yearlySalary = (monthlySalary / 30) * daysWorked * 12;

        return yearlySalary;
    }

    private double calculateTaxAmount(double yearlySalary) {

        if (yearlySalary <= 250000) {
            return 0;
        } else if (yearlySalary > 250000 && yearlySalary <= 500000) {
            return (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary > 500000 && yearlySalary <= 1000000) {
            return 25000 + (yearlySalary - 500000) * 0.1;
        } else {
            return 125000 + (yearlySalary - 1000000) * 0.2;
        }
    }

    private double calculateCessAmount(double yearlySalary) {

        if (yearlySalary > 2500000) {
            double excessAmount = yearlySalary - 2500000;
            return excessAmount * 0.02;
        } else {
            return 0;
        }
    }
}
