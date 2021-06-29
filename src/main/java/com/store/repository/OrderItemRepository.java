package com.store.repository;

import com.store.model.Order;
import com.store.model.OrderItem;
import com.store.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository  extends PagingAndSortingRepository<OrderItem, UUID> {

    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByProduct(Product product);
}
