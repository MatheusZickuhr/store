package com.store.repository;

import com.store.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {
}
