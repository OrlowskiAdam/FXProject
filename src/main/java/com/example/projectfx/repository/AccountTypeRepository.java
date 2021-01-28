package com.example.projectfx.repository;

import com.example.projectfx.model.AccountType;
import com.example.projectfx.model.AccountTypeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> findByName(AccountTypeName accountTypeName);
}
