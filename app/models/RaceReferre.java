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
    Double challengerSpeed = 0.0;
    Double challengeeSpeed = 0.0;
    int challengerMiles = 0;
    int challengeeMiles = 0;
    int challengerPoints = 0;
    int challengedPoints = 0;

    ArrayList<int[]> challenges = new ArrayList<int[]>();

    public RaceReferre(user one, user two){
        this.challenger = one;
        this.challenge = two;
    }

    public void setChallenge(){
        int levelDifference = this.challenge.getUserLevel() - this.challenger.getUserLevel();

        int miles = this.challenger.getAverageDistance();
        Double speed = this.challenger.averageSpeed;
        int miles1 = this.challenge.getAverageDistance();
        Double speed1 = this.challenger.averageSpeed;

        if(this.challenge.getUserLevel()<this.challenger.getUserLevel()){
            this.challengerSpeed = speed * 1.2;
            this.challengeeSpeed = speed1*(levelDifference*0.5);

            this.challengerPoints = 5;
            this.challengedPoints = 5 + (levelDifference * 2);
        }else{
            this.challengerSpeed = speed*(levelDifference*0.5);
            this.challengeeSpeed = speed1 * 1.2;

            this.challengedPoints = 5;
            this.challengerPoints = 5 + (levelDifference * 2);
        }
        this.challengerMiles = this.challenger.getAverageDistance()/2;
        this.challengeeMiles = this.challenge.getAverageDistance()/2;
    }

    public void challengeComplete(Races one, Races two){
        Double oneCS = one.completedSpeed;
        Double twoCS = two.completedSpeed;

        Double oneSpeedDiff = oneCS - this.challenger.getAverageSpeed();
        Double twoSpeedDiff = twoCS - this.challenge.getAverageSpeed();

        if(one.isComplete && two.isComplete){
            if(oneSpeedDiff>twoSpeedDiff){
                one.isWinner(true);
                two.isWinner(false);
                this.challenger.addScore(one.getPoints());
            }else{
                two.isWinner(true);
                one.isWinner(false);
                this.challenge.addScore(two.getPoints());
            }
        }
        this.challenger.updateRaces(one);
        this.challenge.updateRaces(two);
    }

    public int getChallengerMiles(){return this.challengerMiles;}
    public int getChallengedMiles(){return this.challengeeMiles;}
    public Double getChallengerSpeed(){return this.challengerSpeed;}
    public Double getChallengedSpeed(){return this.challengeeSpeed;}

    public user getChallenger(){return this.challenger;}
    public user getChallenge(){return this.challenge;}

    public int getChallengerPoints(){return this.challengerPoints;}
    public int getChallengedPoints(){return this.challengedPoints;}

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
