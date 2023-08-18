package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.login.models.OrdreFabrication;
import com.bezkoder.spring.login.models.SuivieProd;

public interface SuiviProdRepository extends JpaRepository<SuivieProd,Long>  {
    
    SuivieProd findByOrdreFab(OrdreFabrication ordreFabrication);
}
