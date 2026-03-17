package com.hector.orders.service;

import java.util.List;
import java.util.Set;

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;

public interface CustomerService {
    List<CustomerResponse> showCustomers();
    CustomerResponse addCustomer(CustomerRequest dto);
    CustomerResponse searchCustomer(long customerId);
    CustomerResponse updateCustomer(long customerId, CustomerRequest dto);
    void deleteCustomer(long customerId);
    CustomerResponse searchCustomerByOrder(long orderId);
    Set<CustomerResponse> searchCustomersByProduct(long productId);
}
