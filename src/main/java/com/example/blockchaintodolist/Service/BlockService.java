package com.example.blockchaintodolist.Service;

import com.example.blockchaintodolist.Entity.Activity;
import com.example.blockchaintodolist.Entity.Block;
import com.example.blockchaintodolist.Repository.ActivityRepository;
import com.example.blockchaintodolist.Repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final ActivityRepository activityRepository;
    public BlockService(BlockRepository blockRepository, ActivityRepository activityRepository) {
        this.blockRepository = blockRepository;
        this.activityRepository = activityRepository;
        if(!isGenesisBlockAlreadyPresent()){
            addGenesisBlock();
        }
    }

     private String calculateHash(Block block) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

         String dataToHash = block.getBlockId() +
                 block.getTimestamp().formatted(formatter) +
                 block.getActivity().getActivityId() +
                 block.getPreviousTaskHash();

         StringBuilder hexHash = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            for (byte hashByte : hashBytes) {
                hexHash.append(String.format("%02x", hashByte));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexHash.toString();
    }
    private void addGenesisBlock() {
        // Create and save the Activity
        Activity genesisActivity = new Activity();
        genesisActivity.setDescription("Genesis Activity");
        genesisActivity.setDone(true);
        activityRepository.save(genesisActivity);

        // Create the Block
        Block genesis = new Block();
        genesis.setBlockId(0);

        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

        genesis.setTimestamp(formattedTimestamp);
        genesis.setPreviousTaskHash(null);

        // Set the Activity in the Block and save the Block
        genesis.setActivity(genesisActivity);
        genesis.setTaskHash(calculateHash(genesis));
        blockRepository.save(genesis);
    }

    public void addActivity(String description) {
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

        List<Block> blockBlockchain = getTaskBlockchain();
        int blockId = blockBlockchain.size();
        String previousTaskHash = blockBlockchain.get(blockId - 1).getTaskHash();

        // Create and save the Activity
        Activity newActivity = new Activity();
        newActivity.setDescription(description);
        newActivity.setDone(false); // The new task is not marked as done initially
        activityRepository.save(newActivity);

        //Create and save the Block
        Block newBlock = new Block(blockId, formattedTimestamp, newActivity, previousTaskHash);
        newBlock.setTaskHash(calculateHash(newBlock));
        blockRepository.save(newBlock);
    }

    public Activity updateActivity(int activityId) {
        Activity activity = activityRepository.findById(activityId);
        if(activity != null && activity.getActivityId() != 1){
            activity.setDone(true);
            activityRepository.save(activity);
        }else {
            System.out.println("Can't perform the update for this activity!");
        }
        return activity;
    }

    public boolean isBlockchainValid() {
        List<Block> blocks = getTaskBlockchain();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        for (int i = 1; i < blocks.size(); i++) {

            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i - 1);

            String expectedHash = calculateHash(currentBlock);
            String storedHash = currentBlock.getTaskHash();

            System.out.println("Checking Block ID: " + currentBlock.getBlockId());
            System.out.println("Timestamp: " + currentBlock.getTimestamp().formatted(formatter));
            System.out.println("Activity ID: " + currentBlock.getActivity().getActivityId());
            System.out.println("Previous Task Hash: " + currentBlock.getPreviousTaskHash());
            System.out.println("Expected hash: " + expectedHash);
            System.out.println("Stored hash: " + storedHash);
            if (!expectedHash.equals(storedHash) || !currentBlock.getPreviousTaskHash().equals(previousBlock.getTaskHash())) {
                return false;
            }
        }
        return true;
    }

    public List<Block> getTaskBlockchain() {
        return blockRepository.findAll();
    }
    public List<Activity> getActivities(){
        return activityRepository.findAll();
    }

    public void insertMaliciousDataBlock(String malicious) {
        if (getTaskBlockchain().size() >= 2) {
            Random random = new Random();

            int randomNumber = random.nextInt(getTaskBlockchain().size() - 2) + 1;

            Block maliciousBlock = new Block();
            maliciousBlock.setBlockId(randomNumber);
            maliciousBlock.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")));
            maliciousBlock.setPreviousTaskHash(getTaskBlockchain().get(randomNumber).getTaskHash());

            Activity maliciousActivity = new Activity();
            maliciousActivity.setDescription(malicious);
            activityRepository.save(maliciousActivity);
            maliciousBlock.setActivity(maliciousActivity);
            maliciousBlock.setTaskHash(calculateHash(maliciousBlock));

            blockRepository.save(maliciousBlock);

            getTaskBlockchain().set(randomNumber, maliciousBlock);
        } else {
            System.out.println("Blockchain has insufficient blocks to insert malicious data.");
        }
    }

    public boolean isGenesisBlockAlreadyPresent(){
        Block genesis = blockRepository.findById(0);
        if(genesis != null){
            return true;
        }
        return false;
    }
}
