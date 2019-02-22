package com.eriks.service.order.management.service;

import com.eriks.service.order.management.domain.dto.OrderRequest;
import com.eriks.service.order.management.domain.dto.OrderResponse;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface OrderManagementService {

  CompletableFuture<Optional<OrderResponse>> create();

  CompletableFuture<Optional<OrderResponse>> read(final long orderId);

  CompletableFuture<Optional<OrderResponse>> update(final OrderRequest request);

  CompletableFuture<Optional<OrderResponse>> delete(final long orderId);

}
