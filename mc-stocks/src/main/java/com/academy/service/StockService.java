package com.academy.service;

import java.time.LocalDate;

import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;

@SuppressWarnings("rawtypes")
public interface StockService {
	
	ServiceResponse addStock(StockDTO stockDto);
	
	ServiceResponse getAllStocks(String companyCode);
	
	ServiceResponse getLatestStock(String companyCode);
	
	ServiceResponse getLatestStockAllCompanies();
	
	ServiceResponse getStocksWithin(String companyCode, LocalDate startDate, LocalDate endDate);
	
	ServiceResponse deleteStocks(String companyCode);

}
