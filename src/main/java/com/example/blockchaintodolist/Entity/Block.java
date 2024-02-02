package com.example.blockchaintodolist.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blocks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Block {

    @Id
    @Column(name = "id_block")
    private int blockId;
    @Column(name = "timestamp")
    private String timestamp;
    @OneToOne
    @JoinColumn(name = "id_activity")
    private Activity activity;
    @Column(name = "previous_task_hash")
    private String previousTaskHash;
    @Column(name = "task_hash")
    private String taskHash;

    public Block(int blockId, String timestamp, Activity activity, String previousTaskHash) {
        this.blockId = blockId;
        this.timestamp = timestamp;
        this.activity =  activity;
        this.previousTaskHash = previousTaskHash;
    }


}
