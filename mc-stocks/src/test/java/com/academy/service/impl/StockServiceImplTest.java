package com.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.academy.entity.StockEntity;
import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;
import com.academy.repository.StockRepository;

@ExtendWith(SpringExtension.class)
@SuppressWarnings({ "unchecked" })
class StockServiceImplTest {
	
	@InjectMocks
	private StockServiceImpl service;
	
	@Mock
	private StockRepository stockRepository;
	@Mock
	private ModelMapper modelMapper;
	
	private StockDTO stockDto;
	private StockEntity stock;
	private Type type = new TypeToken<List<StockDTO>>() {}.getType();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		stockDto = StockDTO.builder().companyCode("ABC").stockPrice(100.00).build();
		stock = StockEntity.builder().companyCode("XYZ").stockPrice(120.25).build();
	}

	@Test
	void testAddStock() {
		when(stockRepository.save(Mockito.any(StockEntity.class))).thenReturn(new StockEntity());
		ServiceResponse<String> response = service.addStock(stockDto);
		assertNotNull(response, "Add stock response is null");
		assertEquals("Stocks added successfully", response.getBody(), "Response message not matched");
		verify(stockRepository).save(Mockito.any(StockEntity.class));
	}

	@Test
	void testGetAllStocks() {
		when(stockRepository.findByCompanyCodeOrderByTimestampAsc(Mockito.anyString())).thenReturn(Arrays.asList(stock));
		when(modelMapper.map(Mockito.any(List.class), Mockito.eq(type))).thenReturn(Arrays.asList(stockDto));
		ServiceResponse<List<StockDTO>> response = service.getAllStocks("XYZ");
		assertNotNull(response.getBody(), "Get All Stock response is null");
		assertEquals(1, response.getBody().size(), "List size not matched");
		verify(stockRepository).findByCompanyCodeOrderByTimestampAsc(Mockito.anyString());
		verify(modelMapper).map(Mockito.any(List.class), Mockito.eq(type));
	}

	@Test
	void testGetLatestStock() {
		when(stockRepository.findTopByCompanyCodeOrderByTimestampAsc(Mockito.anyString())).thenReturn(Optional.of(stock));
		when(modelMapper.map(Mockito.any(StockEntity.class), Mockito.eq(StockDTO.class))).thenReturn(stockDto);
		ServiceResponse<StockDTO> response = service.getLatestStock("ABC");
		assertNotNull(response.getBody(), "Get Top Stock response is null");
		verify(stockRepository).findTopByCompanyCodeOrderByTimestampAsc(Mockito.anyString());
		verify(modelMapper).map(Mockito.any(StockEntity.class), Mockito.eq(StockDTO.class));
	}
	
	@Test
	void testGetLatestStockNotPresent() {
		when(stockRepository.findTopByCompanyCodeOrderByTimestampAsc(Mockito.anyString())).thenReturn(Optional.empty());
		ServiceResponse<String> response = service.getLatestStock("ABC");
		assertNull(response.getBody(), "Get Top Stock response is not null");
		verify(stockRepository).findTopByCompanyCodeOrderByTimestampAsc(Mockito.anyString());
	}
	
	@Test
	void testGetLatestStockAllCompanies() {
		when(stockRepository.findAll()).thenReturn(Collections.singletonList(stock));
		ServiceResponse<Map<String, StockEntity>> response = service.getLatestStockAllCompanies();
		assertNotNull(response.getBody(), "Get All Stock response is null");
		assertFalse(response.getBody().isEmpty(), "Stock map is empty");
		verify(stockRepository).findAll();
	}

	@Test
	void testGetLatestStockAllCompaniesNoResult() {
		when(stockRepository.findAll()).thenReturn(Collections.emptyList());
		ServiceResponse<Map<String, StockEntity>> response = service.getLatestStockAllCompanies();
		assertNotNull(response.getBody(), "Get All Stock response is null");
		assertTrue(response.getBody().isEmpty(), "Stock map is not empty");
		verify(stockRepository).findAll();
	}

	@Test
	void testGetStocksWithin() {
		when(stockRepository.findByCompanyCodeBetween(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(Arrays.asList(stock));
		when(modelMapper.map(Mockito.any(List.class), Mockito.eq(type))).thenReturn(Arrays.asList(stockDto));
		ServiceResponse<List<StockDTO>> response = service.getStocksWithin("XYZ", LocalDate.now().minusDays(7), LocalDate.now());
		assertNotNull(response.getBody(), "Get Stock within response is null");
		assertEquals(1, response.getBody().size(), "List size not matched");
		verify(stockRepository).findByCompanyCodeBetween(Mockito.anyString(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
		verify(modelMapper).map(Mockito.any(List.class), Mockito.eq(type));
	}

	@Test
	void testDeleteStocks() {
		when(stockRepository.deleteByCompanyCode(Mockito.anyString())).thenReturn(1);
		ServiceResponse<String> response = service.deleteStocks("XYZ");
		assertNotNull(response.getBody(), "Delete Stock response is null");
		assertEquals("Stocks deleted successfully", response.getBody(), "Response message not matched");
		verify(stockRepository).deleteByCompanyCode(Mockito.anyString());
	}
	
	@Test
	void testDeleteStocksFailure() {
		when(stockRepository.deleteByCompanyCode(Mockito.anyString())).thenReturn(0);
		ServiceResponse<String> response = service.deleteStocks("XYZ");
		assertNotNull(response.getBody(), "Delete Stock response is null");
		assertEquals("No Stocks found", response.getBody(), "Response message not matched");
		verify(stockRepository).deleteByCompanyCode(Mockito.anyString());
	}

}
