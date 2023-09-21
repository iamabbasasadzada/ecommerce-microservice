package com.ecommercemicro.inventoryservice.service;

import com.ecommercemicro.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> skuCodes);
}
