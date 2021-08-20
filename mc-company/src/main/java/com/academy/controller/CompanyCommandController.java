package com.academy.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.model.CompanyDTO;
import com.academy.model.ServiceResponse;
import com.academy.service.CompanyService;

@RestController
@RequestMapping("/api/v1.0/market/company")
@SuppressWarnings("rawtypes")
public class CompanyCommandController {
	
	private CompanyService companyService;
	
	public CompanyCommandController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@PostMapping("/register")
	public ServiceResponse registerCompany(@Valid @RequestBody CompanyDTO companyDto) {
		return companyService.registerNewCompany(companyDto);
	}
	
	@DeleteMapping("/delete/{companyCode}")
	public ServiceResponse deleteCompany(@PathVariable("companyCode") String companyCode) {
		return companyService.deleteCompany(companyCode);
	}

}
