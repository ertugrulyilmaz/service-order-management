package com.eriks.service.order.management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvoiceEvent {

  private long orderId;
  private String orderStatus;
  private long date;

}
