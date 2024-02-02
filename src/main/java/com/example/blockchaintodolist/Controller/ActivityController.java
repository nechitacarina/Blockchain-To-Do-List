package com.example.blockchaintodolist.Controller;

import com.example.blockchaintodolist.Entity.Activity;
import com.example.blockchaintodolist.Request.AddActivity;
import com.example.blockchaintodolist.Request.AddMalicious;
import com.example.blockchaintodolist.Service.BlockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ActivityController {

    private final BlockService blockService;

    public ActivityController(BlockService blockService){
         this.blockService = blockService;
    }

    @GetMapping("/activities")
    public List<Activity> getTasks() {
        return blockService.getActivities();
    }

    @PostMapping("/addActivity")
    public AddActivity addTask(@RequestBody AddActivity activity) {
        blockService.addActivity(activity.getDescription());
        return activity;
    }

    @PostMapping("/updateActivity/{id}")
    public Activity updateActivity(@PathVariable int id) {
       return blockService.updateActivity(id);
    }

    @GetMapping("/isValid")
    public String isValid() {
        if (blockService.isBlockchainValid()){
            return "Valid Blockchain";
        }
        return "Invalid Blockchain";
    }

    @PostMapping("/insertMaliciousBlock")
    public AddMalicious insertMaliciousBlock(@RequestBody AddMalicious malicious) {
        blockService.insertMaliciousDataBlock(malicious.getMaliciousBlock());
        //return "Malicious block inserted!";
        return malicious;
    }
}
