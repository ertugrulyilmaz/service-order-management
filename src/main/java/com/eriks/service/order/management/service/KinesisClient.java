package com.eriks.service.order.management.service;

public interface KinesisClient {

  void publishRecord(final long orderId, final String orderStatus);

}
