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
    public ArrayList<Run> runs;
    public int userScore;
    public int userLevel;
    public int averageDistance;
    public int averageSpeed;

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
    public int getAverageDistance(){return this.getAverageDistance();}
    public int getAverageSpeed(){return this.getAverageSpeed();}
    public int getUserLevel(){return this.userLevel;}

    public void challengeSent(String compUsername){ races.add(new Races(compUsername));}
    public void challengeRecieved(String id, String compUsername){ races.add(new Races(id, compUsername));}
    public void findRace(String id){Races r = new Races();}
    public void setRace(String cUsername){races.add(new Races(cUsername));}
    public ArrayList<Races> getRaces(){return this.races;}
}
