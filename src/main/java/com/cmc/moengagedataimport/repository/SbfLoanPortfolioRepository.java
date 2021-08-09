package com.cmc.moengagedataimport.repository;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface SbfLoanPortfolioRepository extends JpaRepository<SbfLoanPortfolio, Integer> {

    @Query(value = "SELECT s FROM SbfLoanPortfolio s WHERE s.data_date > :dataDate")
    List<SbfLoanPortfolio> findAllByData_dateIsGreaterThan(@Param("dataDate") Long dataDate);
}
