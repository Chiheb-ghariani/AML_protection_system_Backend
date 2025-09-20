package com.aml.backend.repository;

import com.aml.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    User findByNationalId(String nationalId);
}
