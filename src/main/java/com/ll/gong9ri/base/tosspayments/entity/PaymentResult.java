package com.ll.gong9ri.base.tosspayments.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResult {
	public String mId;
	public String version;
	public String transactionKey;
	public String lastTransactionKey;
	public String paymentKey;
	public String orderId;
	public String orderName;
	public String status;
	public String requestedAt;
	public String approvedAt;
	public String useEscrow;
	public Boolean cultureExpense;
	public String card;
	public String virtualAccount;
	public String transfer;
	public String mobilePhone;
	public String giftCertificate;
	public String cashReceipt;
	public String cashReceipts;
	public String discount;
	public String cancels;
	public String secret;
	public String type;
	public String easyPay;
	public String country;
	public String failure;
	public Boolean isPartialCancelable;
	public String receipt;
	public Checkout CheckoutObject;
	public String currency;
	public Float totalAmount;
	public Float balanceAmount;
	public Float suppliedAmount;
	public Float vat;
	public Float taxFreeAmount;
	public Float taxExemptionAmount;
	public String method;

	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Checkout {
		public String url;
	}
}