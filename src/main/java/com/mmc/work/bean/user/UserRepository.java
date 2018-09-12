package com.mmc.work.bean.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByTel(String tel);

    User findByEmail(String email);
}
