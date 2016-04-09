package models;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by saadadeel on 07/03/2016.
 */
public class Level {
    public int level;
    public ArrayList<String> usernames = new ArrayList<String>();

    public Level(){}
    public Level(int level){
        this.level = level;
    }

    public void deleteUsername(String u){
        usernames.remove(u);
    }
    public void addUsername(String u){
        usernames.add(u);
    }

    public ArrayList<String> findRandom(int max){
        ArrayList<String> randomUsernames = new ArrayList<String>();
        Random randomGenerator;
        randomGenerator = new Random();
        for(int i = 0; i<max; i++){
            String randomUser =usernames.get(randomGenerator.nextInt(usernames.size()));
            while(randomUsernames.contains(randomUser)){
                System.out.println("yooooo");
                randomUser = usernames.get(randomGenerator.nextInt(usernames.size()));
            }
            randomUsernames.add(randomUser);
        }
        return randomUsernames;
    }
}
