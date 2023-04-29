package edu.itmo.blps.dao.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findCartsByCustomerAndDevice(Integer user,Integer device);
}
