package com.restaurapp.restaurapp.service.product;

import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.domain.repository.ProductRepositoryJpa;
import jakarta.transaction.Transactional;

public class CreateProductService {
    public final ProductRepositoryJpa productRepositoryJpa;

    public CreateProductService(ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
    }

    @Transactional
    public void execute(Product product) {
        if (productRepositoryJpa.findByName(product.getName()) != null) {
            throw new RuntimeException("El producto ya existe");
        }
        productRepositoryJpa.save(product);
    }
}
