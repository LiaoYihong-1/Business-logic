package edu.itmo.blps;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dto.Account;
import edu.itmo.blps.service.AccountService;
import edu.itmo.blps.service.CartService;
import edu.itmo.blps.service.DeviceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@TestPropertySource(
		locations = "classpath:application.properties")
@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationTests {

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private CartService cartService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CompanyRepository companyRepository;

	@Test
	void contextLoads() {

	}

	/*@Test
	@Transactional(value = "bitronixTransactionManager")
	@Rollback
	void addDeviceInCartTest() {
		Account account = new Account("customer", "user1000", "kokl");
		accountService.signup(account);
		Company company = new Company("company", "aaaaaa");
		company = companyRepository.save(company);
		Device device = new Device(null, "transistor", "sslkhfsd", "dsfsjkldhlf", company, 100);
		device = deviceService.saveDevice(device);
		Cart cartFromDb = cartService.putDeviceInCart(device, account.getName());
		Assertions.assertEquals(cartFromDb.getDevices().toArray()[0], device);
	}

	@Test
	@Transactional(value = "bitronixTransactionManager")
	@Rollback
	void removeDeviceFromCartTest() {
		Account account = new Account("customer", "user1000", "kokl");
		accountService.signup(account);

		Company company = new Company("company", "aaaaaa");
		company = companyRepository.save(company);

		Device device1 = new Device(null, "transistor", "sslkhfsd", "dsfsjkldhlf", company, 100);
		device1 = deviceService.saveDevice(device1);

		Device device2 = new Device(null, "resistor", "joly", "jjjjjjjj", company, 99);
		device2 = deviceService.saveDevice(device2);

		Cart cartFromDb = cartService.putDeviceInCart(device1, account.getName());
		cartFromDb.addDevice(device2);
		cartFromDb = cartService.saveCart(cartFromDb);

		cartFromDb = cartService.removeDeviceFromCart(device2, account.getName());

		System.out.println(cartFromDb);
		Assertions.assertEquals(cartFromDb.getDevices().size(), 1);
<<<<<<< HEAD
	}

=======
	}*/
	@Test
	void encoderTest(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("c1"));
		System.out.println(encoder.encode("u2"));
		System.out.println(encoder.encode("u3"));
		System.out.println(encoder.matches("123","$2a$10$9ZQXIrtI9q6gw.G9stoDI.YF/U/D97TAt0BC2nzo6Wz5LGfDMAQJe"));
	}
>>>>>>> parent of 14631e4 (back)
}
