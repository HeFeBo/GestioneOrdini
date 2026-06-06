package com.hector.orders.service.interf;

import java.util.List;
import java.util.Set;

import com.hector.orders.dto.response.ProductResponse;

public interface FinanceService {
    double totalSale();
    double totalSaleByOrder(long orderId);
    double totalSaleByCustomer(long customerId);
    double totalSaleByProduct(long productId);
    double averageTotalSale();
    long totalUnitsSold();
    int totalUnitsSoldByOrder(long orderId);
    int totalUnitsSoldByCustomer(long customerId);
    int totalUnitsSoldByProduct(long productId);
    Set<ProductResponse> showProductsSold();
    List<ProductResponse> bestSellingProducts(int positions);

}
