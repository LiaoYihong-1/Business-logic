package edu.itmo.blps.controller;


import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dto.Account;
import edu.itmo.blps.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@PostMapping("/user/login")
	public ResponseEntity<?> login(@RequestBody User user){
		return accountService.login(user);
	}

	@PostMapping("/hello")
	@PreAuthorize("hasAuthority('company')")
	public String hello(){
		return "Hello\n";
	}

	@PostMapping("/user/signup")
	public ResponseEntity<?> signup(@RequestBody Account request){
		return accountService.signup(request.getName(), request.getPassword(), request.getType());
	}
}
