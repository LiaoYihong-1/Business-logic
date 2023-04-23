package edu.itmo.blps.dao.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
	Optional<Cart> findByUser_Id(Integer id);
	Optional<Cart> findByUser_Username(String username);

}
