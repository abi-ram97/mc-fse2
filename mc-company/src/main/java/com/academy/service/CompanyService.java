package com.academy.service;

import java.util.Set;

import com.academy.model.CompanyDTO;
import com.academy.model.ServiceResponse;

@SuppressWarnings("rawtypes")
public interface CompanyService {
	
	ServiceResponse registerNewCompany(CompanyDTO companyDto);
	
	ServiceResponse getCompanyByCode(String companyCode);
	
	ServiceResponse<Set<CompanyDTO>> getAllCompanies();
	
	ServiceResponse deleteCompany(String companyCode);

}
