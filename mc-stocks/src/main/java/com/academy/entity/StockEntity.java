package com.academy.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "stocks")
@Data
public class StockEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4843810244251058896L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "stock_id")
	private String stockId;
	
	@Column(name = "stock_price")
	private double stockPrice;
	
	@Column(name = "company_code")
	private String companyCode;
	
	@Column(name = "created_date")
	private LocalDate date;
	
	@Column(name = "created_time")
	private LocalTime time;
	
	@CreationTimestamp
	@Column(name = "timestamp")
	private LocalDateTime timestamp;

	public StockEntity() {}
	
	@Builder
	public StockEntity(double stockPrice, String companyCode) {
		this.stockPrice = stockPrice;
		this.companyCode = companyCode;
		this.date = LocalDate.now();
		this.time = LocalTime.now();
	}
	
}
