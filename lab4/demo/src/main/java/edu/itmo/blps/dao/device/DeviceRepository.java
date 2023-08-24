package edu.itmo.blps.dao.device;

import edu.itmo.blps.dao.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Integer> {
	Optional<Device> findDeviceByNameAndCompany(String name, Company company);
	List<Device> findAllByCompany(Company company);
	List<Device> findAllByCompanyId(Integer id);
	@Override
	Optional<Device> findById(Integer integer);

	@Modifying(clearAutomatically = true)
	@Transactional(value = "bitronixTransactionManager")
	@Query("update Device D set D.available = ?2 where D.id = ?1")
	void changeDeviceStatus(Integer id, Boolean status);
}
