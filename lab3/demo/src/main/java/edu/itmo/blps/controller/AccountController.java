package edu.itmo.blps.controller;


import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dto.Account;
import edu.itmo.blps.dto.Response;
import edu.itmo.blps.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AccountController {
	@Autowired
	AccountService accountService;
	@PostMapping("/user/login")
	public ResponseEntity<?> login(@RequestBody User user){
		return accountService.login(user);
	}

	@PostMapping("/user/signup")
	public ResponseEntity<?> signup(@RequestBody Account request){
		return accountService.signup(request.getName(), request.getPassword(), request.getType());
	}
}
