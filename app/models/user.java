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

    @JsonProperty("races")
    public ArrayList<Races> races = new ArrayList<Races>();
    
    @JsonProperty("runs")
    public ArrayList<Run> runs = new ArrayList<Run>();
    public ArrayList<minimalUser> league = new ArrayList<minimalUser>();
    public ArrayList<String> leagueUsernames = new ArrayList<String>();
    public int userScore;
    public int userLevel;
    public int oldUserLevel = 0;
    public double averageDistance;
    public double averageSpeed;

    public user(){
    }

    public user(String fn, String un, String ln, String pw, int initialLevel){
        this.firstName = fn;
        this.username = un;
        this.lastName = ln;
        this.password = pw;
        this.userLevel = initialLevel;
     }

    public String getPassword(){return this.password;}
    public String getFirstName(){return this.firstName;}
    public String getLastName(){return this.firstName;}
    public String getUsername(){return this.username;}
    public double getAverageDistance(){return this.averageDistance;}
    public double getAverageSpeed(){return this.averageSpeed;}
    public int getUserLevel(){return this.userLevel;}

    /////Races////////

    public Races findRace(String id){
        for (Races race : this.races) {
            if (race.getId() == id) {
                return race; //gotcha!
            }
        }
        return null;
    }
    public ArrayList<Races> getRaces(){return this.races;}

    public void addRace(Races races){
        this.races.add(0, races);
    }
    public void updateRaces(Races race){
        for (Races r : this.races) {
            if (r.id.equals(race.getId())) {
                this.races.set(this.races.indexOf(r), race);
            }
        }
    }
    public void deleteRace(String id){
        int index = 0;
        boolean found = false;
        for (Races r : this.races) {
            if (r.id.equals(id)) {
                index = this.races.indexOf(r);
                found = true;
            }
        }
        if(found){
            this.races.remove(index);
        }
    }

    /////Runs////////

    public ArrayList<Run> getRuns(){return this.runs;}
    public void addRun(Run r){
        this.runs.add(0, r);
        r.setScore(this);

        this.updateAverageDistandSpeed();
        this.updateScore();
    }
    public void updateAverageDistandSpeed(){
        double totalDist = 0.0;
        double totalSpeed = 0.0;

        if(this.getRuns()!=null){
            for(int i = 0; i<this.getRuns().size(); i++){
                totalDist += this.getRuns().get(i).getDistance();
                totalSpeed += this.getRuns().get(i).getSpeed();
            }
        }
        System.out.println(totalDist);
        System.out.println(totalSpeed);
        this.averageDistance = totalDist/this.getRuns().size();
        this.averageSpeed = totalSpeed/this.getRuns().size();
    }
    public void setLevel(){
        double avgDist = (this.getAverageDistance())/1000;
        double avgSpeed = (this.getAverageSpeed())*3.6;
        int level = (int)((avgDist*avgSpeed)/10);

        if(level<1){
            level = 1;
        }

        if(level != this.userLevel){
            this.oldUserLevel = this.userLevel;
            this.userLevel = level;
        }else{
            this.userLevel = level;
            this.oldUserLevel = 0;
        }
    }
    public void updateScore(){
        int totalScore = 0;

        if(this.getRuns()!=null){
            for(int i = 0; i<this.getRuns().size(); i++){
                totalScore += this.getRuns().get(i).getScore();
            }
        }
        if(this.getRaces()!=null){
            for(Races race : this.getRaces()){
                if(race.result!=null && race.result.equals("winner")){
                    totalScore += race.getPoints();
                }
            }
        }
        this.userScore = totalScore;
        setLevel();
    }
    public void addScore(int score){
        this.userScore+= score;
    }

    ///// League /////

    public void setleague(ArrayList<minimalUser> mU){
        this.league = mU;
    }
    public void setLeagueUsernames(ArrayList<String> lu){this.leagueUsernames = lu;}
    public void setUserLevel(int level){
        this.userLevel = level;
    }
    public void addLeagueUsernames(String lu){this.leagueUsernames.add(lu);}
    public ArrayList<minimalUser> getLeague(){return this.league;}
    public ArrayList<String> getLeagueUsernames(){return this.leagueUsernames;}
}
