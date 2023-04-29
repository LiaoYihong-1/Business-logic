package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.device.DeviceRepository;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dao.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> getTransaction(Integer userId){
		Optional<User> userOptional = userRepository.findUserById(userId);
		if(!userOptional.isPresent()){
			return ResponseEntity.badRequest().body("Please try to log in again and make suer" +
					"that your account is still available\n");
		}
		User user = userOptional.get();
		return  ResponseEntity.ok(transactionRepository.findByUser(user));
	}

	@Transactional
	@PreAuthorize("hasAuthority('user')")
	public ResponseEntity<?> addTransaction(Integer deviceId,Integer amount, Integer customer){
		Optional<User> userOptional = userRepository.findUserById(customer);
		if(!userOptional.isPresent()){
			return ResponseEntity.badRequest().body("Please login again and check your account still available.\n");
		}
		User user = userOptional.get();

		Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
		if(!deviceOptional.isPresent()){
			return ResponseEntity.badRequest().body("This device not existed");
		}
		Device device = deviceOptional.get();

		Transaction t = new Transaction();
		t.setUser(user);
		t.setStatus("On process");
		t.setSeller(device.getCompany());
		t.setDevice(device);
		t.setAmount(amount);
		t.setDeviceId(deviceId);
		transactionRepository.save(t);
		return ResponseEntity.ok("Success");
	}
}
