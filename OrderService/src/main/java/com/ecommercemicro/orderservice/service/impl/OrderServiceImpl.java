package com.ecommercemicro.orderservice.service.impl;

import com.ecommercemicro.orderservice.dto.InventoryResponse;
import com.ecommercemicro.orderservice.dto.OrderLineItemsDto;
import com.ecommercemicro.orderservice.dto.OrderRequest;
import com.ecommercemicro.orderservice.model.Order;
import com.ecommercemicro.orderservice.model.OrderLineItems;
import com.ecommercemicro.orderservice.repository.OrderRepository;
import com.ecommercemicro.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBUilder;
    @Override
    public String PlaceOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapDtoToOrderLineItems)
                .toList();
        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        InventoryResponse[] inventoryResponses = webClientBUilder.build().get()
                        .uri("http://InventoryService/api/inventory"
                        ,uriBuilder -> uriBuilder.queryParam("skuCodes",skuCodes).build())
                            .retrieve()
                                    .bodyToMono(InventoryResponse[].class)
                                            .block();

        boolean result=  Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (Boolean.TRUE.equals(result)){
            orderRepository.save(order);
            return "Order placed successfully";

        }
        else{
            throw new IllegalArgumentException("Product is not in stock!");
        }

    }

    private OrderLineItems mapDtoToOrderLineItems(OrderLineItemsDto orderLineItemsDto){
            return OrderLineItems.builder()
                    .price(orderLineItemsDto.getPrice())
                    .skuCode(orderLineItemsDto.getSkuCode())
                    .quantity(orderLineItemsDto.getQuantity())
                    .build();
    }
}
