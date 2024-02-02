package com.example.blockchaintodolist.Repository;

import com.example.blockchaintodolist.Entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    Activity findById(int id);
}
