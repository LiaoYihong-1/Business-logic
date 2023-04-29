package edu.itmo.blps.controller;

import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.domain.SecurityUser;
import edu.itmo.blps.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/device")
@RestController
public class DeviceController {
	@Autowired
	private DeviceService deviceService;

	@GetMapping
	public ResponseEntity<?> getAllDevices(@RequestParam(defaultValue = "0") Integer pageNo,
													  @RequestParam(defaultValue = "10") Integer pageSize,
													  @RequestParam(defaultValue = "name") String sortBy) {
		return ResponseEntity.ok(deviceService.getAllDevices(pageNo, pageSize, sortBy));
	}

	@GetMapping("/byDevice/{deviceId}")
	public ResponseEntity<?> getDevice(@PathVariable Integer deviceId) {
		return ResponseEntity.ok(deviceService.getDevice(deviceId));
	}

	@GetMapping("/byCompany/{companyId}")
	public ResponseEntity<?> getAllByCompany(@PathVariable Integer companyId) {
		return deviceService.getAllByCompanyId(companyId);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('company')")
	public ResponseEntity<?> addDevice(@RequestBody Device device) {
		SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return deviceService.saveDevice(device,user.getId());
	}
	@DeleteMapping("{device}")
	@PreAuthorize("hasAuthority('company')")
	public ResponseEntity<?> deleteDevice(@PathVariable Integer device) {
		SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return deviceService.deleteDevice(device,user.getId());
	}
}
