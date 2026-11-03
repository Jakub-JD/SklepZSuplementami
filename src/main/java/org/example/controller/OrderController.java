package org.example.controller;

import org.example.model.Order;
import org.example.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Klasa kontrolera REST dla encji Order w domenie systemu.
 *
 * Udostępnia interfejs sieciowy API (pod adresem /api/orders)
 * do obsługi operacji CRUD.
 */

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return repository.save(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o ID: " + id));
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        return repository.findById(id).map(order -> {
            order.setOrderNumber(updatedOrder.getOrderNumber());
            order.setTotalAmount(updatedOrder.getTotalAmount());
            return repository.save(order);
        }).orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o ID: " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        repository.deleteById(id);
    }
}