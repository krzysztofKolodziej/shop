package com.Shop.shop.repository;

import com.Shop.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existByUsername(String username);
    boolean existByEmail(String email);
    boolean existByPhoneNumber(String phoneNumber);
}
