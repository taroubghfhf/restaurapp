package com.restaurapp.restaurapp.service.product;

import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.domain.repository.ProductRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class UpdateProductService {
    private final ProductRepositoryJpa productRepositoryJpa;

    public UpdateProductService(ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
    }

    @Transactional
    public void execute(Product product) {
        if (productRepositoryJpa.findById(product.getProductId() * 1L).isEmpty()) {
            throw new RuntimeException("El producto no existe");
        }
        productRepositoryJpa.save(product);
    }
}
