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
    double challengerSpeed = 0.0;
    double challengeeSpeed = 0.0;
    double challengerMiles = 0.0;
    double challengeeMiles = 0.0;
    int challengerPoints = 0;
    int challengedPoints = 0;

    ArrayList<int[]> challenges = new ArrayList<int[]>();

    public RaceReferre(user one, user two){
        this.challenger = one;
        this.challenge = two;
    }

    public void setChallenge(){
        int levelDifference = this.challenge.getUserLevel() - this.challenger.getUserLevel();

        double miles = this.challenger.getAverageDistance();
        double speed = this.challenger.averageSpeed;
        double miles1 = this.challenge.getAverageDistance();
        double speed1 = this.challenge.averageSpeed;

        this.challengerPoints = 5;
        this.challengedPoints = 5;

        if(this.challenge.getUserLevel()<this.challenger.getUserLevel()){
            this.challengedPoints = 5 + (levelDifference * 2);

        }else if (this.challenge.getUserLevel()>this.challenger.getUserLevel()){
            this.challengerPoints = 5 + (levelDifference * 2);
        }

        this.challengerMiles = this.challenge.getAverageDistance()/2;
        this.challengeeMiles = this.challenger.getAverageDistance()/2;
        this.challengerSpeed = speed1;
        this.challengeeSpeed = speed;

    }

    public void challengeComplete(Races one, Races two){
        Double oneCS = one.completedSpeed;
        Double twoCS = two.completedSpeed;

        one.status = "complete";
        two.status = "complete";

        if(one.challengeComplete && !two.challengeComplete){
            one.isWinner(true);
            two.isWinner(false);
        }else if(!one.challengeComplete && two.challengeComplete){
            one.isWinner(false);
            two.isWinner(true);
        } else if (one.challengeComplete && two.challengeComplete){
            double perc1 = one.challengedSpeed/oneCS;
            double perc2 = two.challengedSpeed/twoCS;
            if(perc1>perc2){
                one.isWinner(true);
                two.isWinner(false);
            }else{
                one.isWinner(false);
                two.isWinner(true);
            }
        }
        this.challenger.updateRaces(one);
        this.challenge.updateRaces(two);
    }

    public double getChallengerMiles(){return this.challengerMiles;}
    public double getChallengedMiles(){return this.challengeeMiles;}
    public double getChallengerSpeed(){return this.challengerSpeed;}
    public double getChallengedSpeed(){return this.challengeeSpeed;}

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
