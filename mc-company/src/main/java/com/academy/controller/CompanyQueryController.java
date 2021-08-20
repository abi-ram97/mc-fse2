package com.academy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.model.ServiceResponse;
import com.academy.service.CompanyService;

@RestController
@RequestMapping("/api/v1.0/market/company")
@SuppressWarnings("rawtypes")
public class CompanyQueryController {
	private final Logger logger = LoggerFactory.getLogger(CompanyQueryController.class);
	
	private CompanyService companyService;
	
	public CompanyQueryController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping("/info/{companyCode}")
	public ServiceResponse getCompanyByCode(@PathVariable("companyCode") String companyCode) {
		logger.info("Getting Company for [{}]", companyCode);
		return companyService.getCompanyByCode(companyCode);
	}
	
	@GetMapping("/getAll")
	public ServiceResponse getAllCompanies() {
		return companyService.getAllCompanies();
	}
}
