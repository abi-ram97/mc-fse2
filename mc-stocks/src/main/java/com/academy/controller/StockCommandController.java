package com.academy.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;
import com.academy.service.StockService;

@RestController
@RequestMapping(value = "/api/v1.0/market/stock")
@SuppressWarnings("rawtypes")
public class StockCommandController {
	
	private final StockService stockService;
	
	public StockCommandController(StockService stockService) {
		this.stockService = stockService;
	}

	@PostMapping("/add/{companyCode}")
	public ServiceResponse addStock(@Valid @RequestBody StockDTO stockDto, 
			@PathVariable("companyCode") String companyCode) {
		stockDto.setCompanyCode(companyCode);
		return stockService.addStock(stockDto);
	}
	
	@DeleteMapping("/delete/{companyCode}")
	public ServiceResponse deleteStocks(@PathVariable("companyCode") String companyCode) {
		return stockService.deleteStocks(companyCode);
	}

}
