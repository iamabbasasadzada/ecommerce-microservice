package com.ecommercemicro.inventoryservice.service.impl;

import com.ecommercemicro.inventoryservice.dto.InventoryResponse;
import com.ecommercemicro.inventoryservice.repository.InventoryRepository;
import com.ecommercemicro.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCodes) {

        return inventoryRepository.findBySkuCodeIn(skuCodes).stream().map(inventory ->
                    InventoryResponse.builder()
                            .isInStock(inventory.getQuantity()>0)
                            .skuCode(inventory.getSkuCode())
                            .build()

                ).toList();
    }
}
