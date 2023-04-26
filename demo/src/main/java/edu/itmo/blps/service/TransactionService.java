package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.device.DeviceRepository;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dao.transaction.TransactionRepository;
import edu.itmo.blps.dto.Account;
import edu.itmo.blps.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private boolean checkString(String s){
		if ((s == null) | (Objects.equals(s, ""))){
			return false;
		}
		return true;
	}

	@Deprecated
	@Transactional(value = "bitronixTransactionManager")
	public Response addDevice(Device device){
		try {
			deviceRepository.save(device);
			return new Response(true, "Success",null);
		} catch (Exception e) {
			return new Response(false, "Make sure that your account is still available",null);
		}
	}

	@Deprecated
	@Transactional(value = "bitronixTransactionManager")
	public Response addCart(Account account, Device device){
		Cart cart = new Cart();
		cart.setUser(account.asUser());
		cart.addDevice(device);
		try {
			deviceRepository.findDeviceByNameAndCompany(device.getName(), device.getCompany())
					.orElseThrow(() -> new IllegalArgumentException("Device doesn't exist"));
			cartRepository.save(cart);
			return new Response(true, "Success",null);
		}catch (Exception e){
			return new Response(false, "No such kind of device. Maybe this device is deleted.",null);
		}
	}

	@Transactional(value = "bitronixTransactionManager")
	public Response addTransaction(Transaction transaction) {
		try {
			transactionRepository.save(transaction);
			return new Response(true, "Success",null);
		} catch (Exception e) {
			return new Response(false, "Two possibility: You account is not available\nThis device is not deleted",null);
		}
	}

	public List<Transaction> getTransactions(Integer userId){
		return transactionRepository.findByUser_id(userId);
	}
}
