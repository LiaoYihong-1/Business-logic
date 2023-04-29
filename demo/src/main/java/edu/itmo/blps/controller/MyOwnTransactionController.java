package edu.itmo.blps.controller;

import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/transaction")
@RestController
public class MyOwnTransactionController {
	@Autowired
	TransactionService transactionService;

	@PostMapping
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction){
		SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return transactionService.addTransaction(transaction.getDeviceId(), transaction.getAmount(), user.getId());
	}

	@GetMapping
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> getTransaction(){
		SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return transactionService.getTransaction(user.getId());
	}

}
