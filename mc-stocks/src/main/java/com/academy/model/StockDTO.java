package com.academy.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.DecimalMin;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDTO {
	private String stockId;
	
	@DecimalMin(value = "1.0", message = "StockPrice must be greater than 0")
	private double stockPrice;
	
	private String companyCode;
	
	private LocalDate date;
	
	private LocalTime time;
	
	@Builder
	public StockDTO(String stockId, double stockPrice, String companyCode, LocalDate date, LocalTime time) {
		this.stockId = stockId;
		this.stockPrice = stockPrice;
		this.companyCode = companyCode;
		this.date = date;
		this.time = time;
	}


}
