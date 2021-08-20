package com.academy.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.entity.CompanyEntity;
import com.academy.model.CompanyDTO;
import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;
import com.academy.repository.CompanyRepository;
import com.academy.service.CompanyService;
import com.academy.util.ServiceConnector;

@Service
@SuppressWarnings( {"rawtypes"})
@Transactional
public class CompanyServiceImpl implements CompanyService {
	
	private final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	@Value("${services.stock.baseurl}")
	private String stockServiceUrl;
	
	private CompanyRepository companyRepository;
	private ModelMapper modelMapper;
	private ServiceConnector serviceConnector;
	
	public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper, ServiceConnector serviceConnector) {
		this.companyRepository = companyRepository;
		this.modelMapper = modelMapper;
		this.serviceConnector = serviceConnector;
	}

	@Override
	public ServiceResponse registerNewCompany(CompanyDTO companyDto) {
		logger.info("Adding new company for [{}]", companyDto.getCompanyName());
		CompanyEntity company = modelMapper.map(companyDto, CompanyEntity.class);
		CompanyEntity savedCompany = companyRepository.save(company);
		return buildServiceResponse(savedCompany.getCompanyCode(), HttpStatus.CREATED);
	}

	@Override
	public ServiceResponse getCompanyByCode(String companyCode) {
		logger.info("Getting company for [{}]", companyCode);
		Optional<CompanyEntity> optionalCompany = companyRepository.findByCompanyCode(companyCode);
		if(optionalCompany.isPresent()) {
			CompanyDTO company = modelMapper.map(optionalCompany.get(), CompanyDTO.class);
			String url = stockServiceUrl + "/latest/" + company.getCompanyCode();
			ServiceResponse<StockDTO> response = serviceConnector.getApi(url, new ParameterizedTypeReference<ServiceResponse<StockDTO>>() {});
			if (response != null) {
				company.setStocks(Arrays.asList(response.getBody()));
			}
			return buildServiceResponse(company, HttpStatus.OK);
		}
		return buildServiceResponse("Company not found", HttpStatus.NOT_FOUND);
	}

	@Override
	public ServiceResponse<Set<CompanyDTO>> getAllCompanies() {
		logger.info("Getting all company details");
		Set<CompanyDTO> dtos = new HashSet<>();
		Set<CompanyEntity> companies = companyRepository.findAll();
		String url = stockServiceUrl + "/latest";
		Map<String, StockDTO> stockMap = serviceConnector.getApi(url, new ParameterizedTypeReference<Map<String, StockDTO>>() {});
		for(CompanyEntity company : companies) {
			CompanyDTO dto = modelMapper.map(company, CompanyDTO.class);
			if(stockMap != null && stockMap.containsKey(company.getCompanyCode())) {
				dto.addStock(stockMap.get(company.getCompanyCode()));
			}
			dtos.add(dto);
		}
		return buildServiceResponse(dtos, HttpStatus.OK);
	}

	@Override
	public ServiceResponse deleteCompany(String companyCode) {
		logger.info("Deleting company with code [{}]", companyCode);
		try {
			companyRepository.deleteById(companyCode);
			return buildServiceResponse("Company deleted successfully", HttpStatus.OK);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return buildServiceResponse("Company not found", HttpStatus.OK);
	}
	
	private <T>ServiceResponse<T> buildServiceResponse(T body, HttpStatus status) {
		return new ServiceResponse<>(body, status.value());
	}

}
