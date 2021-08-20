package com.academy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
class StockQueryControllerTest {
	
	private final static String BASE_URL = "/api/v1.0/market/stock";
	
	private MockMvc mockMvc;
	private ObjectMapper mapper;
	
	@Mock
	private StockService stockService;
	
	private ServiceResponse response;
	
	private StockDTO stockDto;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new StockQueryController(stockService)).build();
		this.mapper = new ObjectMapper();
		
		stockDto = StockDTO.builder().companyCode("comp1").stockPrice(100.00).build();
	}

	@Test
	void testGetAllStocks() throws Exception {
		response = ServiceResponse.builder().body(Collections.singletonList(stockDto)).status(200).build();
		when(stockService.getAllStocks(Mockito.anyString())).thenReturn(response);
		MvcResult result = this.mockMvc.perform(get(BASE_URL + "/comp1")).andExpect(status().isOk()).andReturn();
		assertNotNull(result.getResponse(), "Get All Stock response is null");
		ServiceResponse<Collection<StockDTO>> actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
		assertFalse(actualResponse.getBody().isEmpty(), "Stock List is empty");
	}

	@Test
	void testGetLatestStockForAllCompanies() throws Exception {
		response = ServiceResponse.builder().body(Arrays.asList(stockDto, stockDto)).status(200).build();
		when(stockService.getLatestStockAllCompanies()).thenReturn(response);
		MvcResult result = this.mockMvc.perform(get(BASE_URL + "/latest")).andExpect(status().isOk()).andReturn();
		assertNotNull(result.getResponse(), "Get All Stock response is null");
		assertNotNull(result.getResponse().getContentAsString(), "Get Response Content null");
		
	}

	@Test
	void testGetLatestStock() throws Exception {
		response = ServiceResponse.builder().body(Collections.singletonList(stockDto)).status(200).build();
		when(stockService.getLatestStock(Mockito.anyString())).thenReturn(response);
		MvcResult result = this.mockMvc.perform(get(BASE_URL + "/latest/comp1")).andExpect(status().isOk()).andReturn();
		assertNotNull(result.getResponse(), "Get Latest Stock response is null");
		ServiceResponse<Collection<StockDTO>> actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
		assertFalse(actualResponse.getBody().isEmpty(), "Stock List is empty");
	}

	@Test
	void testGetStocksWithin() throws Exception {
		stockDto.setDate(LocalDate.now());
		stockDto.setTime(LocalTime.now());
		response = ServiceResponse.builder().body(Arrays.asList(stockDto, stockDto)).status(200).build();
		when(stockService.getStocksWithin(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(response);
		MvcResult result = this.mockMvc.perform(get(BASE_URL + "/get/comp1/2021-01-27/2021-06-27"))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result.getResponse(), "Get Stock Within response is null");
		ServiceResponse<Collection<StockDTO>> actualResponse = mapper.readValue(result.getResponse().getContentAsString(), ServiceResponse.class);
		assertEquals(response.getStatus(), actualResponse.getStatus(), "Response status not matched");
		assertFalse(actualResponse.getBody().isEmpty(), "Stock List is empty");
		assertEquals(2, actualResponse.getBody().size(), "Collection size not matched");
	}

}
