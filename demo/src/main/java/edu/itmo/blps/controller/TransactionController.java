package edu.itmo.blps.controller;

import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dto.Response;
import edu.itmo.blps.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/")
@RestController
public class TransactionController {
	@Autowired
	TransactionService transactionService;

//	@PostMapping("/cart/put-device")
//	public Response addCart(@RequestBody Cart request, Principal principal) {
//		return transactionService.addCart(request);
//	}

	@PostMapping("/add-transaction")
	public Response addTransaction(@RequestBody Transaction request){
		return transactionService.addTransaction(request);
	}

	@GetMapping("/get-transaction/{userId}")
	public List<Transaction> getTransaction(@PathVariable Integer userId){
		return transactionService.getTransactions(userId);
	}
}
