package com.eriks.service.order.management.controller;

import com.eriks.service.order.management.domain.dto.OrderRequest;
import com.eriks.service.order.management.domain.dto.OrderResponse;
import com.eriks.service.order.management.service.OrderManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/v1/order-management", produces = APPLICATION_JSON_VALUE)
public class OrderManagementController {

  private final OrderManagementService orderManagementService;

  public OrderManagementController(final OrderManagementService orderManagementService) {
    this.orderManagementService = orderManagementService;
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity> create() {
    return orderManagementService.create()
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(t -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @GetMapping(value = "/{orderId}")
  public CompletableFuture<ResponseEntity> read(@PathVariable final Long orderId) {
    return orderManagementService.read(orderId)
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(handleGetFailure.apply(orderId));
  }

  @PutMapping(consumes = APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity> update(@RequestBody final OrderRequest request) {
    return orderManagementService.update(request)
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(t -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @DeleteMapping(value = "/{orderId}")
  public CompletableFuture<ResponseEntity> delete(@PathVariable final Long orderId) {
    return orderManagementService.delete(orderId)
    .thenApply(mapMaybeOrderToResponse)
    .exceptionally(handleGetFailure.apply(orderId));
  }

  private static Function<Optional<OrderResponse>, ResponseEntity> mapMaybeOrderToResponse = maybeOrder -> maybeOrder
  .<ResponseEntity>map(ResponseEntity::ok)
  .orElse(ResponseEntity.notFound().build());

  private static Function<Long, Function<Throwable, ResponseEntity>> handleGetFailure = id -> throwable -> {
    log.error(String.format("Unable to retrieve order for id: %s", id), throwable);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  };

}
