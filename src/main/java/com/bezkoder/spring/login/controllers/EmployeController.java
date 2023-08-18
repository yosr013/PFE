package com.bezkoder.spring.login.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.repository.EmployeRepository;
import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Employe;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    //get all employees
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/employees")
    public List<Employe> getAllEmployee(){
        return employeRepository.findAll();
    }

    //create employee rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employees")
public ResponseEntity<?> createEmployee(@RequestBody Employe employe) {
    if (employeRepository.existsByMatricule(employe.getMatricule())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cet employé existe déjà!");
    }
    Employe newEmployee = employeRepository.save(employe);
    return ResponseEntity.status(HttpStatus.CREATED).body("Employé ajouté avec succès !");
}


    //get employee by id rest api
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employe> getEmployeeById(@PathVariable Long id){
        Employe employee = employeRepository.findById(id)
                 .orElseThrow(()-> new ResourceNotFoundException("Employee not found with id : "+id));
        return ResponseEntity.ok(employee);
    }

    //upadte employee rest api
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/employees/{id}")
public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employe employeeDetails) {
    Employe employe = employeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

    if (!employe.getMatricule().equals(employeeDetails.getMatricule())
            && employeRepository.existsByMatricule(employeeDetails.getMatricule())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cet employé existe déjà!");
    }

    employe.setMatricule(employeeDetails.getMatricule());
    employe.setFirstName(employeeDetails.getFirstName());
    employe.setLastName(employeeDetails.getLastName());
    employe.setEmail(employeeDetails.getEmail());
    employe.setNumTel(employeeDetails.getNumTel());

    Employe updatedEmploye = employeRepository.save(employe);
    return ResponseEntity.ok(updatedEmploye);
}


    //delete employee rest api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteEmplopyee(@PathVariable Long id){
        Employe employe=employeRepository.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Employee do not exist with this id:"+id));
        employeRepository.delete(employe);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
    
    
}
