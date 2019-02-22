package com.eriks.service.order.management.repository.impl;

import com.eriks.service.config.AsyncConfig;
import com.eriks.service.order.management.domain.entity.Order;
import com.eriks.service.order.management.repository.OrderManagementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Repository
public class OrderManagementRepositoryImpl implements OrderManagementRepository {

  private final static String SQL_INSERT = "INSERT INTO orders(status, created_at, updated_at) VALUES(:status, :createdAt, :updatedAt) RETURNING *";
  private final static String SQL_FIND_BY_ID = "SELECT id, status, created_at, updated_at FROM orders WHERE id = :orderId";
  private final static String SQL_UPDATE_STATUS_BY_ID = "UPDATE orders SET status = :status, updated_at = :updatedAt WHERE id = :id RETURNING *";

  private final Sql2o sql2o;
  private final Executor executor;

  public OrderManagementRepositoryImpl(final Sql2o sql2o, @Qualifier(AsyncConfig.TASK_EXECUTOR_REPOSITORY) final Executor executor) {
    this.sql2o = sql2o;
    this.executor = executor;
  }

  @Override
  public CompletableFuture<Optional<Order>> create(final Order order) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = sql2o.open()) {
        return Optional.ofNullable(connection.createQuery(SQL_INSERT)
        .setAutoDeriveColumnNames(true)
        .bind(order)
        .executeAndFetchFirst(Order.class));
      }
    }, executor);
  }

  @Override
  public CompletableFuture<Optional<Order>> read(long orderId) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = sql2o.open()) {
        final Order order = connection.createQuery(SQL_FIND_BY_ID)
        .setAutoDeriveColumnNames(true)
        .addParameter("orderId", orderId)
        .executeAndFetchFirst(Order.class);

        return Optional.ofNullable(order);
      }
    }, executor);
  }

  @Override
  public CompletableFuture<Optional<Order>> update(final Order order) {
    return CompletableFuture.supplyAsync(() -> {
      try (Connection connection = sql2o.open()) {
        return Optional.ofNullable(connection.createQuery(SQL_UPDATE_STATUS_BY_ID)
        .setAutoDeriveColumnNames(true)
        .bind(order)
        .executeAndFetchFirst(Order.class));
      }
    }, executor);
  }

}
