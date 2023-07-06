package com.ll.gong9ri.base.tosspayments.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentResult {
	private String mId;
	private String version;
	private String transactionKey;
	private String lastTransactionKey;
	private String paymentKey;
	private String orderId;
	private String orderName;
	private String status;
	private String requestedAt;
	private String approvedAt;
	private String useEscrow;
	private Boolean cultureExpense;
	private String card;
	private String virtualAccount;
	private String transfer;
	private String mobilePhone;
	private String giftCertificate;
	private String cashReceipt;
	private String cashReceipts;
	private String discount;
	private String cancels;
	private String secret;
	private String type;
	private String easyPay;
	private String country;
	private String failure;
	private Boolean isPartialCancelable;
	private String receipt;
	private String checkoutUrl;
	private String currency;
	private Float totalAmount;
	private Float balanceAmount;
	private Float suppliedAmount;
	private Float vat;
	private Float taxFreeAmount;
	private Float taxExemptionAmount;
	private String method;
}