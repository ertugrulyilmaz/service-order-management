package com.eriks.service.order.management.repository;

import com.eriks.service.order.management.domain.entity.Order;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderManagementRepository {

  CompletableFuture<Optional<Order>> create(final Order order);

  CompletableFuture<Optional<Order>> read(final long orderId);

  CompletableFuture<Optional<Order>> update(final Order order);

}
