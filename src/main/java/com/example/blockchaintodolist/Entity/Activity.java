package com.example.blockchaintodolist.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_activity")
    private int activityId;
    @Column(name = "description")
    private String description;
    @Column(name = "is_done")
    private boolean isDone;

    @Override
    public String toString() {
        return "Activity ID: " + getActivityId();
    }
}
