package com.eriks.service.order.management.domain.entity;

import lombok.Data;

@Data
public class Order {

  private long id;
  private String status;
  private long createdAt;
  private long updatedAt;

}
