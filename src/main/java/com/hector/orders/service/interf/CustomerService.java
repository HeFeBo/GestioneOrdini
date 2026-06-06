package com.hector.orders.service.interf;

import java.util.List;

import com.hector.orders.dto.request.CustomerRequest;
import com.hector.orders.dto.response.CustomerResponse;

public interface CustomerService {
    List<CustomerResponse> showCustomers();
    CustomerResponse addCustomer(CustomerRequest dto);
    CustomerResponse searchCustomer(long customerId);
    CustomerResponse updateCustomer(long customerId, CustomerRequest dto);
    void deleteCustomer(long customerId);

}
