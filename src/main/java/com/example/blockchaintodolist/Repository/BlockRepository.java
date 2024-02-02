package com.example.blockchaintodolist.Repository;

import com.example.blockchaintodolist.Entity.Activity;
import com.example.blockchaintodolist.Entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Integer> {
    Block findById(int id);
    Block findByActivity(Activity activity);
}