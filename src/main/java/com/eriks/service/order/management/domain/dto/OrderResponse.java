package com.eriks.service.order.management.domain.dto;

import lombok.Data;

@Data
public class OrderResponse {

  private long orderId;
  private String orderStatus;
  private long createdAt;

}
