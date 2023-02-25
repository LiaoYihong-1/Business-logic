package com.example.demo.Dao.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Repository
public interface TranscationRepository extends JpaRepository<Transaction,Integer> {
    @Query(value = "select T FROM Transaction T where T.customer = ?1")
    List<Transaction> findByCostomer(Integer customer);
}
