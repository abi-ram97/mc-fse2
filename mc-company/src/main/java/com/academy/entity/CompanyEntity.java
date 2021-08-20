package com.academy.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Builder;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "Company")
public class CompanyEntity {
	@DynamoDBHashKey(attributeName = "CompanyCode")
	@DynamoDBAutoGeneratedKey
	private String companyCode;
	@DynamoDBAttribute(attributeName = "CompanyName")
	private String companyName;
	
	@DynamoDBAttribute(attributeName = "CompanyCeo")
	private String companyCeo;
	@DynamoDBAttribute(attributeName = "CompanyWebsite")
	private String companyWebsite;
	
	@DynamoDBAttribute(attributeName = "Turnover")
	private int turnover;
	@DynamoDBAttribute(attributeName = "StockExchange")
	private String stockExchange;
	
	public CompanyEntity() {}
	
	@Builder
	public CompanyEntity(String companyName, String companyCeo, String companyWebsite, int turnover,
			String stockExchange) {
		this.companyName = companyName;
		this.companyCeo = companyCeo;
		this.companyWebsite = companyWebsite;
		this.turnover = turnover;
		this.stockExchange = stockExchange;
	}
	
	
}
