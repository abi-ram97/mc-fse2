package com.academy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.academy.model.CompanyDTO;
import com.academy.model.ServiceResponse;
import com.academy.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@SuppressWarnings({ "unchecked" , "rawtypes"})
class CompanyQueryControllerTest {
	private static final String BASE_URL = "/api/v1.0/market/company";
	
	private MockMvc mockMvc;
	
	private ObjectMapper mapper;
	
	private ServiceResponse response;
	
	private CompanyDTO companyDto;
	
	@Mock
	private CompanyService companyService;
	
	@BeforeAll
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new CompanyQueryController(companyService)).build();
		mapper = new ObjectMapper();
		
		companyDto = new CompanyDTO("XYZ", "John", "xyz.xyz", 0 ,"NSE", Collections.emptyList());
	}

	@Test
	void testGetCompanyByCode() throws Exception {
		response = ServiceResponse.builder().body(companyDto).status(200).build();
		when(companyService.getCompanyByCode(Mockito.anyString())).thenReturn(response);
		MvcResult result = this.mockMvc.perform(get(BASE_URL + "/info/comp1"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Get Company by code response is null");
		ServiceResponse<CompanyDTO> actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
		assertNotNull(actualResponse.getBody(), "Response body is null");
	}

	@Test
	void testGetAllCompanies() throws Exception {
		response = ServiceResponse.builder().body(Collections.singleton(companyDto)).status(200).build();
		when(companyService.getAllCompanies()).thenReturn(response);
		MvcResult result = this.mockMvc.perform(get(BASE_URL + "/getAll"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Get All Company response is null");
		ServiceResponse<Collection<CompanyDTO>> actualResponse = mapper.readValue(result.getResponse().getContentAsString(),  ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
		assertNotNull(actualResponse.getBody(), "Response body is null");
		assertFalse(actualResponse.getBody().isEmpty(), "Get all response list is empty");
	}

}
