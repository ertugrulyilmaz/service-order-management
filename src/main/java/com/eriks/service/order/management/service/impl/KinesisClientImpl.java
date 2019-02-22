package com.eriks.service.order.management.service.impl;

import com.eriks.service.config.model.KinesisProperties;
import com.eriks.service.order.management.domain.dto.InvoiceEvent;
import com.eriks.service.order.management.service.KinesisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;
import software.amazon.kinesis.common.KinesisClientUtil;

import java.nio.ByteBuffer;
import java.util.Date;

@Slf4j
@Service
public class KinesisClientImpl implements KinesisClient {

  private final String streamName;
  private final KinesisAsyncClient kinesisAsyncClient;
  private final ObjectMapper objectMapper;

  public KinesisClientImpl(final KinesisProperties kinesisProperties, final ObjectMapper objectMapper) {
    this.streamName = kinesisProperties.getStreamName();
    this.kinesisAsyncClient = KinesisClientUtil.createKinesisAsyncClient(KinesisAsyncClient.builder().region(kinesisProperties.getRegion()));
    this.objectMapper = objectMapper;
  }

  @Override
  public void publishRecord(final long orderId, final String orderStatus) {
    try {
      final InvoiceEvent event = new InvoiceEvent(orderId, orderStatus, new Date().getTime());
      ByteBuffer data = ByteBuffer.wrap(objectMapper.writeValueAsBytes(event));

      final PutRecordRequest request = PutRecordRequest.builder()
      .partitionKey(String.valueOf(orderId))
      .streamName(streamName)
      .data(SdkBytes.fromByteArray(data.array()))
      .build();

      log.info("{}, {}, {}", streamName, orderId, orderStatus, objectMapper.writeValueAsString(event));

      kinesisAsyncClient.putRecord(request);
    } catch (JsonProcessingException e) {
      log.error("{}", e);
    }
  }

}
