package com.restaurapp.restaurapp.service.product;

import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.domain.repository.ProductRepositoryJpa;

import java.util.List;

public class GetProductService {
    private final ProductRepositoryJpa productRepositoryJpa;

    public GetProductService(ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
    }
    public List<Product> execute(){
      return this.productRepositoryJpa.findAll();
    }
}
