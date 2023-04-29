package edu.itmo.blps.service;

import edu.itmo.blps.dao.company.Company;
import edu.itmo.blps.dao.company.CompanyRepository;
import edu.itmo.blps.dao.device.Device;
import edu.itmo.blps.dao.device.DeviceRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	public List<Device> getAllDevices(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC,sortBy));
		Page<Device> pagedResult = deviceRepository.findAll(paging);
		return pagedResult.getContent();
	}

	@Transactional
	public ResponseEntity<?> getDevice(Integer id) {
		Optional<Device> o = deviceRepository.findById(id);
		if(o.isPresent()){
			return ResponseEntity.ok(o.get());
		}else {
			return ResponseEntity.badRequest().body("This device is deleted in database");
		}
	}

	@Transactional
	public Device getDeviceOrThrow(Integer id) {
		return deviceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Device with id = " + id + " was not found"));
	}

	@Transactional
	public Device getDeviceOrThrow(Company company, String deviceName) {
		return deviceRepository.findDeviceByNameAndCompany(deviceName, company)
				.orElseThrow(() -> new EntityNotFoundException("Device was not found: " + deviceName));
	}

	@Transactional
	public ResponseEntity<?> getAllByCompanyId(Integer id) {
		Optional<Company> o = companyRepository.findById(id);
		Company c;
		try{
			c = o.get();
		}catch (NoSuchElementException e){
			return ResponseEntity.badRequest().body("no such company");
		}
		return ResponseEntity.ok(deviceRepository.findAllByCompany(c));
	}

	/*public List<Device> getAllByCompanyId(Integer companyId) {
		return deviceRepository.findAllByCompany_Id(companyId);
	}*/

	@Transactional
	public ResponseEntity<?> saveDevice(Device device,Integer id) {
		Optional<Company> company = companyRepository.findById(id);
		try{
			Company c = company.get();
			device.setCompany(c);
		}catch (NoSuchElementException e){
			return  ResponseEntity.badRequest().body("Check your company account is still accessible");
		}
		deviceRepository.save(device);
		return ResponseEntity.ok("Success");
	}

	@Transactional
	public ResponseEntity<?> deleteDevice(Integer deviceId,Integer id) {
		Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
		if(deviceOptional.isPresent()){
			Device device = deviceOptional.get();
			if(!device.getCompany().getId().equals(id)){
				return ResponseEntity.badRequest().body("You are trying to delete a device," +
						"which is not belong to your company");
			}
			deviceRepository.delete(device);
		}else {
			return ResponseEntity.badRequest().body("No such a device");
		}
		return ResponseEntity.ok("Success");
	}
}
