package com.academy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.academy.entity.StockEntity;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, String> {
	
	List<StockEntity> findByCompanyCodeOrderByTimestampAsc(String companyCode);
	
	Optional<StockEntity> findTopByCompanyCodeOrderByTimestampAsc(String companyCode);
	
	int deleteByCompanyCode(String companyCode);
	
	@Query("select s from StockEntity s where s.companyCode=?1 and s.date between ?2 and ?3")
	List<StockEntity> findByCompanyCodeBetween(String companyCode, LocalDate startDate, LocalDate endDate);
	
}
