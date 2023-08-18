package com.bezkoder.spring.login.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.login.models.Machine;

public interface MachineRepository extends JpaRepository<Machine,Long> {

    Page<Machine> findByChaineId(Long chaineId, Pageable pageable);
    
    boolean existsByDesignation(String designation);
}
