package com.cmc.moengagedataimport.repository;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SbfLoanPortfolioRepository extends JpaRepository<SbfLoanPortfolio, Integer> {
}
