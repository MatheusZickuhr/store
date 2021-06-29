package com.store.controller;

import com.store.model.OrderItem;
import com.store.model.Product;
import com.store.repository.OrderItemRepository;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Product> allProducts(Model model) {
        return productRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{pageNumber}")
    public Iterable<Product> allProductsPaginated(@PathVariable int pageNumber) {
        final int pageSize = 5;

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Product> pagedResult = productRepository.findAll(paging);

        return pagedResult.toList();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createOrUpdateProduct(@RequestBody Product product) {

        ResponseEntity<Product> responseEntity = ResponseEntity
                .status(product.getId() == null ? HttpStatus.CREATED : HttpStatus.OK)
                .body(product);

        productRepository.save(product);

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@RequestBody Product product) {
        List<OrderItem> orderItemList = orderItemRepository.findByProduct(product);

        if (orderItemList.size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cannot delete product in use");
        }

        productRepository.delete(product);

        return ResponseEntity.status(HttpStatus.OK)
                .body(product);
    }
}