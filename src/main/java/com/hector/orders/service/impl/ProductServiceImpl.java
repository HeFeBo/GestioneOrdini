package com.hector.orders.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hector.orders.dto.request.ProductRequest;
import com.hector.orders.dto.response.ProductResponse;
import com.hector.orders.exception.ProductNotFoundException;
import com.hector.orders.mapper.ProductMapper;
import com.hector.orders.model.Product;
import com.hector.orders.repository.ProductRepo;
import com.hector.orders.service.interf.ProductService;

import lombok.RequiredArgsConstructor;

@Qualifier("ProductServiceImpl")
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> showProducts() {
        return productRepo.findAll().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductResponse addProduct(ProductRequest dto) {   
        Product product = productMapper.toEntity(dto);
        if(product == null) throw new RuntimeException(); // creare un'eccezione personalizzata.
        Product saved = productRepo.save(product);

        return productMapper.toDTO(saved);
    }

    @Override
    public ProductResponse searchProduct(long productoId) {
        Product product = productRepo.findById(productoId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productoId + " non trovato."));
        return productMapper.toDTO(product);
    }

    @Override
    public ProductResponse updateProduct(long productoId, ProductRequest dto) {
        Product product = productRepo.findById(productoId).orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + productoId + " non trovato."));
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setOrigin(dto.getOrigin());
        product.setPrice(dto.getPrice());
        product.setCost(dto.getCost());
        product.setPurchaseDate(dto.getPurchaseDate());

        Product updated = productRepo.save(product);
        
        return productMapper.toDTO(updated);
    }

    @Override
    public void deleteProduct(long idProducto) {
        productRepo.deleteById(idProducto);
    }

}
