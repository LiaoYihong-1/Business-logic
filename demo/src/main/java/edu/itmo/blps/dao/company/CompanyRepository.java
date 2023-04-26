package edu.itmo.blps.dao.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {
    @Query(value="select C from  Company  C where C.password=?1 and C.name = ?2")
    List<Company> login(String password, String name);

    @Query(value = "select C.name from Company C")
    List<String> findAllNames();

    Optional<Company> findByNameAndPassword(String password, String name);

    boolean existsByName(String name);
}
