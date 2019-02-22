package com.eriks.service.order.management.service.impl;

import com.eriks.service.order.management.domain.dto.OrderRequest;
import com.eriks.service.order.management.domain.dto.OrderResponse;
import com.eriks.service.order.management.domain.entity.Order;
import com.eriks.service.order.management.repository.OrderManagementRepository;
import com.eriks.service.order.management.service.KinesisClient;
import com.eriks.service.order.management.service.OrderManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@Service
public class OrderManagementServiceImpl implements OrderManagementService {

  private final OrderManagementRepository orderManagementRepository;
  private final KinesisClient kinesisClient;

  public OrderManagementServiceImpl(final OrderManagementRepository orderManagementRepository, final KinesisClient kinesisClient) {
    this.orderManagementRepository = orderManagementRepository;
    this.kinesisClient = kinesisClient;
  }

  @Override
  public CompletableFuture<Optional<OrderResponse>> create() {
    final Order order = new Order();
    order.setStatus("PAYMENT_WAITED");
    order.setCreatedAt(new Date().getTime());

    return orderManagementRepository.create(order)
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(handleFailure);
  }

  @Override
  public CompletableFuture<Optional<OrderResponse>> read(final long orderId) {
    return orderManagementRepository.read(orderId)
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(handleFailure);
  }

  @Override
  public CompletableFuture<Optional<OrderResponse>> update(final OrderRequest orderRequest) {
    final Order order = new Order();
    order.setId(orderRequest.getId());
    order.setStatus(orderRequest.getStatus());
    order.setUpdatedAt(new Date().getTime());

    return orderManagementRepository.update(order)
    .thenApply(maybeOrder ->
    maybeOrder.map(entity -> {
      final OrderResponse orderResponse = new OrderResponse();
      orderResponse.setOrderId(entity.getId());
      orderResponse.setOrderStatus(entity.getStatus());
      orderResponse.setCreatedAt(entity.getCreatedAt());

      kinesisClient.publishRecord(entity.getId(), entity.getStatus());

      return orderResponse;
    }))
    .exceptionally(handleFailure);
  }

  @Override
  public CompletableFuture<Optional<OrderResponse>> delete(final long orderId) {
    final Order order = new Order();
    order.setId(orderId);
    order.setStatus("CANCELLED");
    order.setUpdatedAt(new Date().getTime());

    return orderManagementRepository.update(order)
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(handleFailure);
  }

  private Function<Optional<Order>, Optional<OrderResponse>> mapMaybeOrderToResponse = maybeOrder ->
  maybeOrder.map(entity -> {
    final OrderResponse orderResponse = new OrderResponse();
    orderResponse.setOrderId(entity.getId());
    orderResponse.setOrderStatus(entity.getStatus());
    orderResponse.setCreatedAt(entity.getCreatedAt());

    return orderResponse;
  });

  private Function<Throwable, Optional<OrderResponse>> handleFailure = throwable -> {
    log.error("{}", throwable);

    return Optional.empty();
  };

}
