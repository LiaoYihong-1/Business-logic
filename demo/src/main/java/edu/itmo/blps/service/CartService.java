package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;
	@Transactional
	public ResponseEntity<?> getCartByUsernameOrThrow(Integer id) {
		List<Cart> cartList = userRepository.findUserById(id).get().getMyCarts();
		return ResponseEntity.ok(cartList);
	}
	@Transactional
	public ResponseEntity<?> addCart(Integer user,Integer device){
		Cart cart = new Cart();
		cart.setCustomer(user);
		cart.setDevice(device);
		cartRepository.save(cart);
		return ResponseEntity.ok(cart);
	}
	@Transactional
	public ResponseEntity<?> deleteCart(Integer user,Integer device){
		Optional<Cart> cartOptional = cartRepository.findCartsByCustomerAndDevice(user,device);
		Cart cart;
		try{
			cart = cartOptional.get();
		}catch (NoSuchElementException e){
			return ResponseEntity.badRequest().body("Failed delete");
		}
		cartRepository.delete(cart);
		return ResponseEntity.ok("Success");
	}
}
