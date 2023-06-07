package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.customer.User;
import edu.itmo.blps.dao.customer.UserRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.device.DeviceRepository;
import edu.itmo.blps.dao.message.Message;
import edu.itmo.blps.dao.message.MessageRepository;
import edu.itmo.blps.dao.transaction.Transaction;
import edu.itmo.blps.dao.transaction.TransactionRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
public class TransactionService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Transactional(value = "bitronixTransactionManager")
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

	@Transactional(value = "bitronixTransactionManager")
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

	@Qualifier("kafkaConsumer2")
	@Autowired
	KafkaConsumer<String, String> consumer;
	//@KafkaListener(topics = "my-topic",groupId = "my-group")
	@Transactional(value = "bitronixTransactionManager",rollbackFor = Exception.class)
	public void receiveMessage() {
		//System.out.println(message);

		// 拉取消息
		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));

		// 处理消息
		for (ConsumerRecord<String, String> record : records) {
			System.out.println("Received message 2: " + record.value());
			Set<Integer> users = new HashSet<>();
			List<Transaction> transactions = transactionRepository.findAll();
			String[] split = record.value().split(":");
			Integer id = Integer.valueOf(split[1]);
			for(Transaction t :transactions){
				if(t.getDevice().getId().equals(id)){
					Message m = new Message();
					m.setCustomer(t.getUser().getId());
					m.setLife(30);
					users.add(t.getUser().getId());
					if(!Boolean.valueOf(split[0])) {
						transactionRepository.changeTransactionStatus(t.getId(),"Hung up");
						m.setText("Sorry, your device is not available because of some unpredicted\nYou can cancel it or wait for some days\n");
						messageRepository.save(m);
					}else if(t.getStatus().equals("Hung up")){
						transactionRepository.changeTransactionStatus(t.getId(),"On process");
						m.setText("Your transaction is recovery. Thank for your waiting");
						messageRepository.save(m);
					}
				}
			}
		}
	}
}
