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
    public double averageDistance;
    public double averageSpeed;

    public user(){}

    public user(String fn, String un, String ln, String pw){
        this.firstName = fn;
        this.username = un;
        this.lastName = ln;
        this.password = pw;
        this.userLevel = 2;
    }

    public String getPassword(){return this.password;}
    public String getFirstName(){return this.firstName;}
    public String getUsername(){return this.username;}
    public double getAverageDistance(){return this.averageDistance;}
    public double getAverageSpeed(){return this.averageSpeed;}
    public int getUserLevel(){return this.userLevel;}

    /////Races////////

    public void challengeSent(String compUsername){ races.add(new Races(compUsername));}
    public Races findRace(String id){
        for (Races race : this.races) {
            if (race.getId() == id) {
                return race; //gotcha!
            }
        }
        return null;
    }
    public void setRace(String cUsername){races.add(new Races(cUsername));}
    public void acceptRace(String cUsername){races.add(new Races("yes",cUsername));}
    public ArrayList<Races> getRaces(){return this.races;}

    public void addRace(Races races){
        this.races.add(0, races);
        updateScore();
    }
    public void updateRaces(Races race){
        for (Races r : this.races) {
            if (race.getId() == r.id) {
                this.races.set(this.races.indexOf(r), race);
            }
        }
    }

    /////Runs////////

    public ArrayList<Run> getRuns(){return this.runs;}
    public void addRun(Run r){
        this.runs.add(0, r);
        this.updateScore();
    }
    public void updateScore(){
        int[] runScores = new int[this.getRuns().size()];
        int[] raceScore = new int[this.getRaces().size()];
        int totalScore = 0;

        if(this.getRuns()!=null){
            for(int i = 0; i<this.getRuns().size(); i++){
                totalScore += this.getRuns().get(i).getScore();
            }
        }
        if(this.getRaces()!=null){
            raceScore = new int[this.getRaces().size()];
            for(int i = 0; i<this.getRaces().size(); i++){
                if(this.getRaces().get(i).isComplete) {
                    totalScore += this.getRaces().get(i).points;
                }
            }
        }
        this.userScore = totalScore;
    }
    public void addScore(int score){
        this.userScore+= score;
    }

    ///// League /////

    public void setleague(ArrayList<minimalUser> mU){
        this.league = mU;
    }
    public void setLeagueUsernames(ArrayList<String> lu){this.leagueUsernames = lu;}
    public void addLeagueUsernames(String lu){this.leagueUsernames.add(lu);}
    public ArrayList<minimalUser> getLeague(){return this.league;}
    public ArrayList<String> getLeagueUsernames(){return this.leagueUsernames;}
}
