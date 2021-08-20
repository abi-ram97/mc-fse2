package com.academy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.academy.model.CompanyDTO;
import com.academy.model.ServiceResponse;
import com.academy.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SuppressWarnings({ "unchecked" , "rawtypes"})
@TestInstance(Lifecycle.PER_CLASS)
class CompanyCommandControllerTest {
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
				.standaloneSetup(new CompanyCommandController(companyService)).build();
		mapper = new ObjectMapper();
		
		companyDto = new CompanyDTO("XYZ", "John", "xyz.xyz", 0 ,"NSE", Collections.emptyList());
	}

	@Test
	void testRegisterCompany() throws Exception {
		response = ServiceResponse.builder().body("company1").status(201).build();
		when(companyService.registerNewCompany(Mockito.any(CompanyDTO.class))).thenReturn(response);
		MvcResult result = this.mockMvc.perform(post(BASE_URL + "/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(companyDto)))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Register company response is null");
		ServiceResponse<String> actualResponse = mapper.readValue(
				result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
	}
	
	@Test
	void testRegisterCompanyValidationFailure() throws Exception {
		companyDto.setCompanyName(null);
		this.mockMvc.perform(post(BASE_URL + "/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(companyDto)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}
	

	@Test
	void testDeleteCompany() throws Exception {
		response = ServiceResponse.builder().body("Successfully deleted company1").status(200).build();
		when(companyService.deleteCompany(Mockito.anyString())).thenReturn(response);
		MvcResult result = this.mockMvc.perform(delete(BASE_URL + "/delete/company1"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Delete company response is null");
		ServiceResponse<String> actualResponse = mapper.readValue(
				result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
		assertEquals(response.getBody(), actualResponse.getBody(), "Response body not matched");
	}

}
