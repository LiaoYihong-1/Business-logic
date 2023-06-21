package edu.itmo.blps.dao.transaction;

import edu.itmo.blps.dao.customer.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    List<Transaction> findByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query("update Transaction t set t.status = ?2 where t.id = ?1")
    void changeTransactionStatus(Integer id, String status);
}
