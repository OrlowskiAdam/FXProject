package com.example.projectfx.repository;

import com.example.projectfx.model.Mail;
import com.example.projectfx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    Optional<Mail> findFirstByOrderByIdDesc();
    List<Mail> findAllByAddresseeIdOrderByReceiveDateTimeDesc(Long id);
    List<Mail> findAllBySender(User sender);
    List<Mail> findAllByAddresseeAndIsDeletedOrderByIdDesc(User addressee, boolean isDeleted);
}
