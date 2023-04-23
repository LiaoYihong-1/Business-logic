package edu.itmo.blps.controller;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/{username}/cart")
@RestController
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping
	@PreAuthorize("#username == authentication.name")
	public ResponseEntity<Cart> getCart(@PathVariable String username){
		return ResponseEntity.ok(cartService.getCartByUsernameOrThrow(username));
	}

	@PutMapping("/put-device")
	@PreAuthorize("#username == authentication.name")
	public ResponseEntity<Cart> putDeviceToCart(@RequestBody Device device, @PathVariable String username) {
		return ResponseEntity.ok(cartService.putDeviceInCart(device, username));
	}

	@DeleteMapping("/remove-device/{deviceId}")
	@PreAuthorize("#username == authentication.name")
	public ResponseEntity<Cart> removeDeviceFromCart(@PathVariable Integer deviceId, @PathVariable String username) {
		return ResponseEntity.ok(cartService.removeDeviceFromCart(deviceId, username));
	}
}
