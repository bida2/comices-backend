package com.vuetify.entities;



import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public class PriceAtDate {
	private UUID priceId;
	private String monthOfYear;
	private float priceAtMonth;
	
	PriceAtDate() {}
	
	public PriceAtDate(String monthOfYear, float priceAtMonth) {
		this.priceId = UUID.randomUUID();
		this.monthOfYear = monthOfYear;
		this.priceAtMonth = priceAtMonth;
	}

	public UUID getPriceId() {
		return priceId;
	}

	public String getMonthOfYear() {
		return monthOfYear;
	}

	public void setMonthOfYear(String monthOfYear) {
		this.monthOfYear = monthOfYear;
	}

	public float getPriceAtMonth() {
		return priceAtMonth;
	}

	public void setPriceAtMonth(float priceAtMonth) {
		this.priceAtMonth = priceAtMonth;
	}
	
	
	
}
