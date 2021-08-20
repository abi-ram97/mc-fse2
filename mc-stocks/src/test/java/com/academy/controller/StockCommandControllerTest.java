package com.academy.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;
import com.academy.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SuppressWarnings({ "unchecked" , "rawtypes"})
class StockCommandControllerTest {
	
	private final static String BASE_URL = "/api/v1.0/market/stock";
	
	private MockMvc mockMvc;
	private ObjectMapper mapper;
	
	@Mock
	private StockService stockService;
	
	private ServiceResponse response;
	
	private StockDTO stockDto;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new StockCommandController(stockService)).build();
		this.mapper = new ObjectMapper();
		
		stockDto = StockDTO.builder().companyCode("comp1").stockPrice(100.0).build();
	}

	@Test
	void testAddStock() throws Exception {
		response = ServiceResponse.builder().body("Stocks added successfully").status(200).build();
		when(stockService.addStock(Mockito.any(StockDTO.class))).thenReturn(response);
		MvcResult result = this.mockMvc.perform(post(BASE_URL + "/add/comp1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(stockDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Add Stock response is null");
		ServiceResponse<String> actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getBody(), actualResponse.getBody(), "Response body not matched");
	}
	
	@Test
	void testAddStockValidation() throws Exception {
		stockDto.setStockPrice(0.0);
		response = ServiceResponse.builder().body("Stocks added successfully").status(200).build();
		when(stockService.addStock(Mockito.any(StockDTO.class))).thenReturn(response);
		MvcResult result = this.mockMvc.perform(post(BASE_URL + "/add/comp1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(stockDto)))
				.andExpect(status().isBadRequest())
				.andReturn();
		assertNotNull(result.getResponse(), "Add Stock response is null");
	}
	
	@Test
	void testDeleteStocks() throws Exception {
		response = ServiceResponse.builder().body("Stocks deleted successfully").status(200).build();
		when(stockService.deleteStocks(Mockito.anyString())).thenReturn(response);
		MvcResult result = this.mockMvc.perform(delete(BASE_URL + "/delete/company1"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Delete Stock response is null");
		ServiceResponse<String> actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getBody(), actualResponse.getBody(), "Response body not matched");
	}
	
}
