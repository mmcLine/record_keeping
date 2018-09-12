package com.mmc.work.bean.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface  OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByTradeDateBetween(LocalDate startDate,LocalDate endDate);

    @Query(value = "SELECT DATE_FORMAT(t.trade_date,'%Y-%m') MONTH,SUM(t.amt) amtsum  FROM trade_order t where t.user=?1 GROUP BY MONTH",nativeQuery=true)
    List<Object[]> findMonthDataByUser(Integer user);

}
