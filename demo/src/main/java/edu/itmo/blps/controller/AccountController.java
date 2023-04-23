package edu.itmo.blps.controller;


import edu.itmo.blps.dto.Account;
import edu.itmo.blps.dto.Response;
import edu.itmo.blps.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AccountController {
	@Autowired
	AccountService accountService;

	@PostMapping("/login")
	public Response login(@RequestBody Account request){
		return accountService.login(request.getName(), request.getPassword(), request.getType());
	}

	@PostMapping("/register")
	public Response signup(@RequestBody Account request){
		return accountService.signup(request.getName(), request.getPassword(), request.getType());
	}
}
