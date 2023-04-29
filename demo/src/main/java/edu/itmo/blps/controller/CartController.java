package edu.itmo.blps.controller;
import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/cart")
@RestController
public class CartController {

	@Autowired
	private CartService cartService;

	@GetMapping
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> getCart(){
		SecurityUser user = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return cartService.getCartByUsernameOrThrow(user.getId());
	}
	@PostMapping("/{device}")
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> addCart(@PathVariable Integer device){
		SecurityUser user = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return cartService.addCart(user.getId(),device);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> deleteCart(@PathVariable Integer id){
		SecurityUser user = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return cartService.deleteCart(user.getId(),id);
	}
}
