package com.maxcheung.camelsimple.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "tradeDate", "marginAmount", "excessAmount", "requiredAmount" })
@Entity
public class SgxMargin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

    @Column
	private String tradeDate;
    @Column
	private BigDecimal marginAmount;
    @Column
	private BigDecimal excessAmount;
    @Column
	private BigDecimal requiredAmount;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	public BigDecimal getMarginAmount() {
		return marginAmount;
	}
	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}
	public BigDecimal getExcessAmount() {
		return excessAmount;
	}
	public void setExcessAmount(BigDecimal excessAmount) {
		this.excessAmount = excessAmount;
	}
	public BigDecimal getRequiredAmount() {
		return requiredAmount;
	}
	public void setRequiredAmount(BigDecimal requiredAmount) {
		this.requiredAmount = requiredAmount;
	}

    
    
    
}