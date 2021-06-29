package com.store.controller;

import com.store.model.Order;
import com.store.model.OrderItem;
import com.store.model.Product;
import com.store.repository.OrderItemRepository;
import com.store.repository.OrderRepository;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("order_items")
public class OrderItemController {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderItemController(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<OrderItem> allOrdersItems(Model model) {
        return orderItemRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public Object getOrderItem(@PathVariable("id") UUID id) {
        return orderItemRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{pageNumber}/{pageSize}")
    public Iterable<OrderItem> allOrderItemsPaginated(@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {


        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<OrderItem> pagedResult = orderItemRepository.findAll(paging);

        return pagedResult.toList();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createOrUpdateOrderItem(@RequestBody OrderItem orderItem) {

        // fetch the order and product
        Order order = orderRepository.findById(orderItem.getOrder().getId()).get();
        Product product = productRepository.findById(orderItem.getProduct().getId()).get();

        if (!product.isEnabled()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cannot add to a order a disabled product");
        }

        if (order.isFinished()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cannot add order item to a finished order");
        }

        ResponseEntity<OrderItem> responseEntity = ResponseEntity
                .status(orderItem.getId() == null ? HttpStatus.CREATED : HttpStatus.OK)
                .body(orderItem);

        orderItemRepository.save(orderItem);

        return responseEntity;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Object deleteOrderItem(@RequestBody OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
        return orderItem;
    }
}
