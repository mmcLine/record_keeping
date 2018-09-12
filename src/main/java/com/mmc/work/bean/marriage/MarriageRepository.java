package com.mmc.work.bean.marriage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarriageRepository extends JpaRepository<Marriage,Integer> {

    Marriage findByUser(Integer user);

    List<Marriage> findByFamily(Integer family);

}
