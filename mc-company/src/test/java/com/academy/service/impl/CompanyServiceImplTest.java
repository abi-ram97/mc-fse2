package com.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.academy.entity.CompanyEntity;
import com.academy.model.CompanyDTO;
import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;
import com.academy.repository.CompanyRepository;
import com.academy.util.ServiceConnector;

@ExtendWith(SpringExtension.class)
@SuppressWarnings({ "unchecked" })
class CompanyServiceImplTest {
	
	@InjectMocks
	private CompanyServiceImpl service;
	@Mock
	private CompanyRepository companyRepository;
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private ServiceConnector serviceConnector;
	
	private CompanyDTO companyDto;
	private CompanyEntity company;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		companyDto = CompanyDTO.builder().companyCeo("John").companyName("XYZ")
				.companyWebsite("xyz.net")
				.stocks(new ArrayList<>())
				.build();
		company = CompanyEntity.builder()
				.companyCeo("Ann").companyName("ABC")
				.companyWebsite("abc.com").build();
	}

	@Test
	void testRegisterNewCompany() {
		company.setCompanyCode(UUID.randomUUID().toString());
		when(modelMapper.map(Mockito.any(CompanyDTO.class), Mockito.eq(CompanyEntity.class))).thenReturn(company);
		when(companyRepository.save(Mockito.any(CompanyEntity.class))).thenReturn(company);
		ServiceResponse<String> response = service.registerNewCompany(companyDto);
		assertNotNull(response, "Register Company response is null");
		assertNotNull(response.getBody(), "Company Code is null");
		verify(companyRepository).save(Mockito.any(CompanyEntity.class));
	}

	@Test
	void testGetCompanyByCode() {
		ServiceResponse<StockDTO> stockResponse = new ServiceResponse<>(StockDTO.builder().build(), 200);
		when(companyRepository.findByCompanyCode(Mockito.anyString())).thenReturn(Optional.of(company));
		when(modelMapper.map(Mockito.any(CompanyEntity.class), Mockito.eq(CompanyDTO.class))).thenReturn(companyDto);
		when(serviceConnector.getApi(Mockito.anyString(), Mockito.any())).thenReturn(stockResponse);
		ServiceResponse<CompanyDTO> response = service.getCompanyByCode("ABC");
		assertNotNull(response.getBody(), "Get Company response is null");
		assertFalse(response.getBody().getStocks().isEmpty());
		verify(companyRepository).findByCompanyCode(Mockito.anyString());
	}
	
	@Test
	void testGetCompanyByCodeFailure() {
		when(companyRepository.findByCompanyCode(Mockito.anyString())).thenReturn(Optional.empty());
		ServiceResponse<String> response = service.getCompanyByCode("ABC");
		assertNotNull(response.getBody(), "Get Company response is null");
		assertEquals("Company not found", response.getBody(), "Response message not matched");
		verify(companyRepository).findByCompanyCode(Mockito.anyString());
	}
	
	@Test
	void testGetCompanyByCodeStockError() {
		when(companyRepository.findByCompanyCode(Mockito.anyString())).thenReturn(Optional.of(company));
		when(modelMapper.map(Mockito.any(CompanyEntity.class), Mockito.eq(CompanyDTO.class))).thenReturn(companyDto);
		when(serviceConnector.getApi(Mockito.anyString(), Mockito.any())).thenReturn(null);
		ServiceResponse<CompanyDTO> response = service.getCompanyByCode("ABC");
		assertNotNull(response.getBody(), "Get Company response is null");
		verify(companyRepository).findByCompanyCode(Mockito.anyString());
	}

	@Test
	void testGetAllCompanies() {
		company.setCompanyCode("comp1");
		when(companyRepository.findAll()).thenReturn(Collections.singleton(company));
		when(modelMapper.map(Mockito.any(CompanyEntity.class), Mockito.eq(CompanyDTO.class))).thenReturn(companyDto);
		when(serviceConnector.getApi(Mockito.anyString(), Mockito.any())).thenReturn(Collections.singletonMap("comp1", new StockDTO()));
		ServiceResponse<Set<CompanyDTO>> response = service.getAllCompanies();
		assertNotNull(response.getBody(), "Get All Company response is null");
		assertEquals(1, response.getBody().size(), "Get All companies size not matched");
		verify(companyRepository).findAll();
	}
	
	@Test
	void testGetAllCompaniesNoStock() {
		company.setCompanyCode("comp1");
		when(companyRepository.findAll()).thenReturn(Collections.singleton(company));
		when(modelMapper.map(Mockito.any(CompanyEntity.class), Mockito.eq(CompanyDTO.class))).thenReturn(companyDto);
		when(serviceConnector.getApi(Mockito.anyString(), Mockito.any())).thenReturn(Collections.emptyMap());
		ServiceResponse<Set<CompanyDTO>> response = service.getAllCompanies();
		assertNotNull(response.getBody(), "Get All Company response is null");
		assertEquals(1, response.getBody().size(), "Get All companies size not matched");
		verify(companyRepository).findAll();
	}

	@Test
	void testGetAllCompaniesError() {
		company.setCompanyCode("comp1");
		when(companyRepository.findAll()).thenReturn(Collections.singleton(company));
		when(modelMapper.map(Mockito.any(CompanyEntity.class), Mockito.eq(CompanyDTO.class))).thenReturn(companyDto);
		when(serviceConnector.getApi(Mockito.anyString(), Mockito.any())).thenReturn(null);
		ServiceResponse<Set<CompanyDTO>> response = service.getAllCompanies();
		assertNotNull(response.getBody(), "Get All Company response is null");
		assertEquals(1, response.getBody().size(), "Get All companies size not matched");
		verify(companyRepository).findAll();
	}
	
	@Test
	void testDeleteCompany() {
		doNothing().when(companyRepository).deleteById(Mockito.anyString());
		ServiceResponse<String> response = service.deleteCompany("ABC");
		assertNotNull(response.getBody(), "Delete Company response is null");
		assertEquals("Company deleted successfully", response.getBody(), "Response message not matched");
		verify(companyRepository).deleteById(Mockito.anyString());
	}
	
	@Test
	void testDeleteCompanyError() {
		doThrow(NullPointerException.class).when(companyRepository).deleteById(Mockito.anyString());
		ServiceResponse<String> response = service.deleteCompany("ABC");
		assertNotNull(response.getBody(), "Delete Company response is null");
		assertEquals("Company not found", response.getBody(), "Response message not matched");
		verify(companyRepository).deleteById(Mockito.anyString());
	}

}
