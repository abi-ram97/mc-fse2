package com.academy.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDTO {
	private String companyCode;
	
	@NotNull(message = "CompanyName cannot be null")
	private String companyName;
	
	@NotNull(message = "CompanyCeo cannot be null")
	private String companyCeo;
	
	@NotNull(message = "CompanyWebsite cannot be null")
	private String companyWebsite;
	
	private int turnover;
	
	@NotNull(message = "StockExchange cannot be null")
	private String stockExchange;
	
	private List<StockDTO> stocks = new ArrayList<>();
	
	@Builder
	public CompanyDTO(String companyName, String companyCeo, String companyWebsite, int turnover,
			String stockExchange, List<StockDTO> stocks) {
		this.companyName = companyName;
		this.companyCeo = companyCeo;
		this.companyWebsite = companyWebsite;
		this.turnover = turnover;
		this.stockExchange = stockExchange;
		this.stocks = stocks;
	}
	
	public void addStock(StockDTO stock) {
		stocks.add(stock);
	}
}
