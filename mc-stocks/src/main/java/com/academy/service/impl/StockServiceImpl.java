package com.academy.service.impl;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.academy.entity.StockEntity;
import com.academy.model.ServiceResponse;
import com.academy.model.StockDTO;
import com.academy.repository.StockRepository;
import com.academy.service.StockService;

@Service
@SuppressWarnings("rawtypes")
@Transactional
public class StockServiceImpl implements StockService {
	private final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);
	
	private StockRepository stockRepository;
	private ModelMapper modelMapper;
	private Type stockDtoType;
	
	public StockServiceImpl(StockRepository stockRepository, ModelMapper modelMapper) {
		this.stockRepository = stockRepository;
		this.modelMapper = modelMapper;
		this.stockDtoType = new TypeToken<List<StockDTO>>() {}.getType();
	}

	@Override
	public ServiceResponse addStock(StockDTO stockDto) {
		logger.info("Adding stock with price [{}] for [{}]", stockDto.getStockPrice(),
				stockDto.getCompanyCode());
		StockEntity stock = StockEntity.builder()
				.companyCode(stockDto.getCompanyCode()) 
				.stockPrice(stockDto.getStockPrice())
				.build();
		stockRepository.save(stock);
		
		return buildServiceResponse("Stocks added successfully", HttpStatus.CREATED);
	}

	@Override
	public ServiceResponse getAllStocks(String companyCode) {
		List<StockEntity> stocks = stockRepository.findByCompanyCodeOrderByTimestampAsc(companyCode);
		List<StockDTO> stockDtos = modelMapper.map(stocks, stockDtoType);
		logger.info("Found [{}] stocks for Company [{}]", stocks.size(), companyCode);
		return buildServiceResponse(stockDtos, HttpStatus.OK);
	}

	@Override
	public ServiceResponse getLatestStock(String companyCode) {
		Optional<StockEntity> optionalStock = stockRepository.findTopByCompanyCodeOrderByTimestampAsc(companyCode);
		if(optionalStock.isPresent()) {
			StockDTO stock = modelMapper.map(optionalStock.get(), StockDTO.class);
			return buildServiceResponse(stock, HttpStatus.OK);
		}
		return buildServiceResponse(null, HttpStatus.OK);
	}

	@Override
	public ServiceResponse getLatestStockAllCompanies() {
		List<StockEntity> stocks = stockRepository.findAll();
		Map<String, StockEntity> resultMap = new HashMap<>();
		Map<String, List<StockEntity>> companyMap = stocks.stream().collect(Collectors.groupingBy(StockEntity::getCompanyCode));
		for(Entry<String, List<StockEntity>> entry : companyMap.entrySet()) {
			String companyCode = entry.getKey();
			Collections.sort(entry.getValue(), 
					Comparator.comparing(StockEntity::getTimestamp).reversed());
			StockEntity stock = entry.getValue().get(0);
			resultMap.put(companyCode, stock);
		}
		return buildServiceResponse(resultMap, HttpStatus.OK);
	}

	@Override
	public ServiceResponse getStocksWithin(String companyCode, LocalDate startDate, LocalDate endDate) {
		List<StockEntity> stocks = stockRepository.findByCompanyCodeBetween(companyCode, startDate, endDate);
		List<StockDTO> stockDtos = modelMapper.map(stocks, stockDtoType);
		logger.info("Found [{}] stocks for Company [{}]", stocks.size(), companyCode);
		return buildServiceResponse(stockDtos, HttpStatus.OK);
	}

	@Override
	public ServiceResponse deleteStocks(String companyCode) {
		int rowsDeleted = stockRepository.deleteByCompanyCode(companyCode);
		if(rowsDeleted != 0) {
			return buildServiceResponse("Stocks deleted successfully", HttpStatus.OK);
		}
		return buildServiceResponse("No Stocks found", HttpStatus.OK);
	}
	
	private <T>ServiceResponse<T> buildServiceResponse(T body, HttpStatus status) {
		return new ServiceResponse<>(body, status.value());
	}

}
