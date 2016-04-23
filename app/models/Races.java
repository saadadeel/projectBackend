package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Races {
    public String status;
    public String id;
    public String competitorUsername;

    @JsonProperty("challengedMiles")
    public double challengedDist;

    @JsonProperty("challengedSpeed")
    public double challengedSpeed;
    public Boolean isComplete = false;
    public double completedSpeed;

    public String result;
    public int points;
    public int compUserLevel;
    public boolean challengeComplete;

        public Races(){}

        public Races(String cUN){
            this.status = "pending";
            this.competitorUsername = cUN;
        }

        public Races(String st, String cUN, String status){
            this.status = status;
            this.competitorUsername = cUN;
            this.id = st;
        }

    public String getId(){return this.id;}
    public void setChallengedDist(double m){ this.challengedDist = m;}
    public void setChallengedSpeed(double s){ this.challengedSpeed = s;}
    public void setPoints(int p){this.points = p;}
    public int getPoints(){return this.points;}
    public void setIsWinner(Boolean result){
        if(result){
            this.result = "WON";
        }else{
            this.result = "LOST";
        }
    }
    public void setCompUserLevel(int a){
        this.compUserLevel = a;
    }

    }
