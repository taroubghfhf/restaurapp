package com.restaurapp.restaurapp.controller.product;

import com.restaurapp.restaurapp.domain.model.Phone;
import com.restaurapp.restaurapp.domain.model.Product;
import com.restaurapp.restaurapp.service.phone.GetPhonesService;
import com.restaurapp.restaurapp.service.product.CreateProductService;
import com.restaurapp.restaurapp.service.product.DeleteProductService;
import com.restaurapp.restaurapp.service.product.GetProductService;
import com.restaurapp.restaurapp.service.product.UpdateProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    private final CreateProductService createProductService;
    private final GetProductService getProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;

    public ProductController(CreateProductService createProductService, GetProductService getProductService,
                             UpdateProductService updateProductService, DeleteProductService deleteProductService) {
        this.createProductService = createProductService;
        this.getProductService = getProductService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product){
        createProductService.execute(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @GetMapping
    public ResponseEntity<List<Product>> getProduct(){
      return ResponseEntity.status(HttpStatus.OK).body(getProductService.execute());
    }
    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){
        updateProductService.execute(product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping
    public ResponseEntity<Product> delete(@RequestBody Product product){
        deleteProductService.execute(product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
