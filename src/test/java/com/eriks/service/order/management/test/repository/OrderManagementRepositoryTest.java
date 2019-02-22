package com.eriks.service.order.management.test.repository;

import com.eriks.service.order.management.domain.entity.Order;
import com.eriks.service.order.management.repository.OrderManagementRepository;
import com.eriks.service.order.management.repository.impl.OrderManagementRepositoryImpl;
import com.eriks.service.order.management.test.ApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = { ApplicationTest.class })
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "/drop_schema.sql", "/schema.sql", "/data_orders.sql"})
public class OrderManagementRepositoryTest {

  private final Executor executor = Executors.newSingleThreadExecutor();

  @Autowired
  private Sql2o sql2o;

  @Test
  public void shouldReturnEmptyIfIdDoesNotExist() {
    final OrderManagementRepository orderManagementRepository = new OrderManagementRepositoryImpl(sql2o, executor);

    final Optional<Order> maybeEntity = orderManagementRepository.read(111L).join();

    assertEquals(maybeEntity, Optional.empty());
  }

  @Test
  public void shouldReturnEmptyIfIdDoesNotExist2() {
    final String SQL_FIND_BY_ID = "SELECT id, status, created_at, updated_at FROM orders WHERE id = :orderId";

    final Sql2o mockSql2o = mock(Sql2o.class);
    final Connection mockConnection = mock(Connection.class);
    final Query mockQuery = mock(Query.class);
    final OrderManagementRepository orderManagementRepository = new OrderManagementRepositoryImpl(mockSql2o, executor);

    when(mockSql2o.open()).thenReturn(mockConnection);
    when(mockConnection.createQuery(SQL_FIND_BY_ID)).thenReturn(mockQuery);
    when(mockQuery.setAutoDeriveColumnNames(true)).thenReturn(mockQuery);
    when(mockQuery.addParameter("orderId", 111L)).thenReturn(mockQuery);
    when(mockQuery.executeAndFetchFirst(Order.class)).thenReturn(null);

    final Optional<Order> maybeEntity = orderManagementRepository.read(111L).join();

    assertEquals(maybeEntity, Optional.empty());

    verify(mockSql2o).open();
    verify(mockConnection).createQuery(SQL_FIND_BY_ID);
    verify(mockQuery).setAutoDeriveColumnNames(true);
    verify(mockQuery).executeAndFetchFirst(Order.class);
  }

}
