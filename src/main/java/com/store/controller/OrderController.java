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

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Order> allOrders(Model model) {
        return orderRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{pageNumber}")
    public Iterable<Order> allOrdersPaginated(@PathVariable int pageNumber) {
        final int pageSize = 5;

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Order> pagedResult = orderRepository.findAll(paging);

        return pagedResult.toList();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createOrUpdateOrder(@RequestBody Order order) {

        boolean orderHasId = order.getId() != null;

        orderRepository.save(order);

        // if the order is finished, update the order price
        if (order.isFinished()) {
            order.setPrice(0.0f);

            List<OrderItem> orderItemList = orderItemRepository.findByOrder(order);

            for (OrderItem orderItem : orderItemList) {
                Product product = orderItem.getProduct();

                float orderPrice = order.getPrice();

                // applies the discount if is not a service (product)
                if (!product.isService()) {
                    orderPrice += product.getPrice() - (product.getPrice() * order.getDiscountRate());
                } else {
                    orderPrice += product.getPrice();
                }

                order.setPrice(orderPrice);
            }
        }

        orderRepository.save(order);

        return ResponseEntity
                .status(orderHasId ? HttpStatus.OK : HttpStatus.CREATED)
                .body(order);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Object deleteOrder(@RequestBody Order order) {
        orderRepository.delete(order);
        return order;
    }
}
