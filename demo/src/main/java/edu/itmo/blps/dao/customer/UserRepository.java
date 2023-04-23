package edu.itmo.blps.dao.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
//    @Query(value="select C from User C where C.password=?1 and C.name = ?2")
//    List<User> login(String password, String name);

    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
