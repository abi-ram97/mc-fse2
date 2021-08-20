package com.academy.repository;

import java.util.Optional;
import java.util.Set;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.academy.entity.CompanyEntity;

@Repository
@EnableScan
public interface CompanyRepository extends CrudRepository<CompanyEntity, String> {
	Optional<CompanyEntity> findByCompanyCode(String companyCode);
	
	Set<CompanyEntity> findAll();
	
	void deleteByCompanyCode(String companyCode);
}
