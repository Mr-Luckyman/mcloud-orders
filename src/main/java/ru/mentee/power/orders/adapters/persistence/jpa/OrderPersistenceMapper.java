package ru.mentee.power.orders.adapters.persistence.jpa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.domain.model.OrderLine;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderPersistenceMapper {

    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "kafkaOffset", source = "kafkaOffset")
    @Mapping(target = "lines", ignore = true)
    @Mapping(target = "processedAt", expression = "java(java.time.Instant.now())")
    OrderEntity toEntity(Order order, String eventId, Long kafkaOffset);

    @Mapping(target = "order", ignore = true)
    OrderLineEntity toLineEntity(OrderLine line);
}