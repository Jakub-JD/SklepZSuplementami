package org.example.repository;

import org.example.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfejs repozytorium dla encji Order w domenie systemu.
 *
 * Zapewnia automatyczną implementację podstawowych operacji bazodanowych
 * dostarczaną przez Spring Data JPA.
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}