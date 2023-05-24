package edu.itmo.blps.dao.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    @Modifying(clearAutomatically = true)
    @Query("update Message M set M.life = ?2 where M.id = ?1")
    public void updateMessageByLife(Integer id, Integer life);
}
