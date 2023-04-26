package edu.itmo.blps.service;

import edu.itmo.blps.dao.cart.Cart;
import edu.itmo.blps.dao.cart.CartRepository;
import edu.itmo.blps.dao.device.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private CartRepository cartRepository;

	@Transactional(value = "bitronixTransactionManager")
	public Cart saveCart(Cart cart) {
		return cartRepository.save(cart);
	}

	public Cart getCartByUsernameOrThrow(String username) {
		return cartRepository.findByUser_Username(username)
				.orElseThrow(() -> new EntityNotFoundException("Cart was not found for this username=" + username));
	}

	public Cart findByUsername(String username) {
		return cartRepository.findByUser_Username(username)
				.orElseThrow(() -> new EntityNotFoundException("Cart was not found for this username=" + username));
	}

	@Transactional(value = "bitronixTransactionManager")
	public Cart putDeviceInCart(Device device, String username) {
		Cart cart = findByUsername(username);
		cart.addDevice(deviceService.findDevice(device));
		return cartRepository.save(cart);
	}

	@Transactional(value = "bitronixTransactionManager")
	public Cart removeDeviceFromCart(Device device, String username) {
		device = deviceService.findDevice(device);
		Cart cart = findByUsername(username);
		if (cart.getDevices().contains(device)) {
			cart.removeDevice(device);
			return cartRepository.save(cart);
		}
		else
			throw new EntityNotFoundException("Cart doesn't contain device: " + device);
	}
	
	
	public boolean existDeviceInCart(Integer deviceId, String username) {
		return findByUsername(username).getDevices().contains(deviceService.getDevice(deviceId));
	}

	public Optional<Device> getDeviceIfPresentInCart(Integer deviceId, String username) {
		Device deviceFromDb = deviceService.getDevice(deviceId);
		if (Objects.isNull(deviceFromDb))
			return Optional.empty();
		Cart cart = findByUsername(username);
		if (cart.getDevices().contains(deviceFromDb))
			return Optional.of(deviceFromDb);
		return Optional.empty();
	}

	@Transactional(value = "bitronixTransactionManager")
	public Cart removeDeviceFromCart(Integer deviceId, String username) {
		Device device = deviceService.getDeviceOrThrow(deviceId);
		Cart cart = findByUsername(username);
		if (cart.getDevices().contains(device)) {
			cart.removeDevice(device);
			return cartRepository.save(cart);
		}
		else
			throw new EntityNotFoundException("Cart doesn't contain device: " + deviceId);
	}
}
