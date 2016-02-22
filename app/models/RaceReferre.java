package models;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by saadadeel on 21/02/2016.
 */
public class RaceReferre{
    String id;
    user challenger;
    user challenge;

    ArrayList<int[]> challenges = new ArrayList<int[]>();

    public RaceReferre(user one, user two){
        this.challenger = one;
        this.challenge = two;
    }

    public void setChallenge(){
        int levelDifference = this.challenge.getUserLevel() - this.challenger.getUserLevel();
        int[] ch = new int[2];
        int miles = this.challenger.getAverageDistance();;
        int time = this.challenger.getAverageDistance()/(this.challenger.averageSpeed);
        int miles1 = this.challenge.getAverageDistance();;
        int time1 = this.challenge.getAverageDistance()/(this.challenger.averageSpeed);

        if(levelDifference<0){
            time = time;
            time1 = time1/levelDifference;
        }else{
            time1 = time1;
            time = time/levelDifference;
        }

        ch[0] = miles;
        ch[1] = time;
        this.challenges.add(ch);
        ch[0] = miles1;
        ch[1] = time1;
        this.challenges.add(ch);
    }

    public ArrayList<int[]> getChallenges(){
        return this.challenges;
    }

    public void sendChallenge(){
        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);
        MongoCollection users = jongo.getCollection("users");
        user user1 = users.findOne("{'username':" + this.challenge + "}").as(user.class);
//        user1.challengeRecieved(id, challenger);
    }

    public void challengeAccepted(){
        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);
        MongoCollection users = jongo.getCollection("users");
        user user = users.findOne("{'username':" + this.challenger + "}").as(user.class);

    }
}
