package com.academy.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
public class StockDTO {
	private String stockId;
	
	private double stockPrice;
	
	private String companyCode;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime time;
	
	public StockDTO() {}

	@Builder
	public StockDTO(String stockId, double stockPrice, String companyCode, LocalDate date, LocalTime time) {
		this.stockId = stockId;
		this.stockPrice = stockPrice;
		this.companyCode = companyCode;
		this.date = date;
		this.time = time;
	}


}
