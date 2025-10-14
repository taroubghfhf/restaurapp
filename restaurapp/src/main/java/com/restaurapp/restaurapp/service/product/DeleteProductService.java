package com.restaurapp.restaurapp.service.product;

import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.domain.repository.ProductRepositoryJpa;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class DeleteProductService {
    private final ProductRepositoryJpa productRepositoryJpa;

    public DeleteProductService(ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
    }

    @Transactional
    public void execute(Product product) {
        if (this.productRepositoryJpa.findById(product.getProductId() * 1L).isEmpty()) {
            throw new RuntimeException("El producto no existe");
        }
        this.productRepositoryJpa.delete(product);
    }
}
