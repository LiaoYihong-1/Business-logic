package edu.itmo.blps.controller;

import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/device")
@RestController
public class DeviceController {
	@Autowired
	private DeviceService deviceService;

	@GetMapping
	public ResponseEntity<List<Device>> getAllDevices(@RequestParam(defaultValue = "0") Integer pageNo,
													  @RequestParam(defaultValue = "10") Integer pageSize,
													  @RequestParam(defaultValue = "name") String sortBy) {
		return ResponseEntity.ok(deviceService.getAllDevices(pageNo, pageSize, sortBy));
	}

	@GetMapping("/{deviceId}")
	public ResponseEntity<Device> getDevice(@PathVariable Integer deviceId) {
		return ResponseEntity.ok(deviceService.getDevice(deviceId));
	}

	@GetMapping("/{companyId}")
	public ResponseEntity<List<Device>> getAllByCompany(@PathVariable Integer companyId) {
		return ResponseEntity.ok(deviceService.getAllByCompanyId(companyId));
	}

	@PostMapping
	public ResponseEntity<Device> addDevice(@RequestBody Device device) {
		return ResponseEntity.ok(deviceService.saveDevice(device));
	}
}
