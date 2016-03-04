package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saadadeel on 26/01/2016.
 */

public class user{
    public String firstName;
    public String lastName;
    public String username;
    public String password;
    public String[] league;
    @JsonProperty("races")
    public ArrayList<Races> races = new ArrayList<Races>();
    @JsonProperty("runs")
    public ArrayList<Run> runs = new ArrayList<Run>();
    public int userScore;
    public int userLevel;
    public int averageDistance;
    public Double averageSpeed;

    public user(){}

    public user(String fn, String un, String ln, String pw){
        this.firstName = fn;
        this.username = un;
        this.lastName = ln;
        this.password = pw;
    }

    public String getPassword(){return this.password;}
    public String getFirstName(){return this.firstName;}
    public String getUsername(){return this.username;}
    public int getAverageDistance(){return this.averageDistance;}
    public Double getAverageSpeed(){return this.averageSpeed;}
    public int getUserLevel(){return this.userLevel;}

    public void challengeSent(String compUsername){ races.add(new Races(compUsername));}
    public void challengeRecieved(String id, String compUsername){ races.add(new Races(id, compUsername));}
    public void findRace(String id){Races r = new Races();}
    public void setRace(String cUsername){races.add(new Races(cUsername));}
    public void acceptRace(String cUsername){races.add(new Races("yes",cUsername));}
    public ArrayList<Races> getRaces(){return this.races;}

    public ArrayList<Run> getRuns(){return this.runs;}
    public void addRun(Run r){
        this.runs.add(0, r);
    }
    public void updateScore(){
        int[] runScores;
        int totalScore = 0;
        if(this.getRuns()!=null){
            runScores = new int[this.getRuns().size()];
            for(int i = 0; i<this.getRuns().size(); i++){
                totalScore += this.getRuns().get(i).getScore();
            }
        }
        this.userScore = totalScore;
    }
//    public void updateAverageDistandSpeed(){
//        int[] runDist;
//        int[] runTime;
//        int totalDist = 0;
//        int totalTime = 0;
//        if(this.getRuns()!=null){
//            runDist = new int[this.getRuns().size()];
//            runTime = new int[this.getRuns().size()];
//            for(int i = 0; i<this.getRuns().size(); i++){
//                totalDist += this.getRuns().get(i).getDistance();
//                totalTime += this.getRuns().get(i).getTime();
//            }
//        }
//        this.averageDistance = totalDist;
//        this.averageSpeed = totalDist/totalTime;
//    }
}
