package com.academy.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.model.ServiceResponse;
import com.academy.service.StockService;

@RestController
@RequestMapping(value = "/api/v1.0/market/stock", produces = MediaType.APPLICATION_JSON_VALUE)
@SuppressWarnings("rawtypes")
public class StockQueryController {
	private final StockService stockService;
	
	public StockQueryController(StockService stockService) {
		this.stockService = stockService;
	}
	
	@GetMapping("/{companyCode}")
	public ServiceResponse getAllStocks(@PathVariable("companyCode") String companyCode) {
		return stockService.getAllStocks(companyCode);
	}
	
	@GetMapping("/latest")
	public ResponseEntity getLatestStockForAllCompanies() {
		ServiceResponse response = stockService.getLatestStockAllCompanies();
		return ResponseEntity.ok(response.getBody());
	}
	
	@GetMapping("/latest/{companyCode}")
	public ServiceResponse getLatestStock(@PathVariable("companyCode") String companyCode) {
		return stockService.getLatestStock(companyCode);
	}
	
	@GetMapping("/get/{companyCode}/{startDate}/{endDate}")
	public ServiceResponse getStocksWithin(@PathVariable("companyCode") String companyCode,
			@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		return stockService.getStocksWithin(companyCode, startDate, endDate);
	}
}
