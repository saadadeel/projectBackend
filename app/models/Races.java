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
    public double challengedMiles;

    @JsonProperty("challengedSpeed")
    public double challengedSpeed;
    public Boolean isComplete = false;
    public double completedMiles;
    public double completedSpeed;

    @JsonProperty("speedChallengeCompleted")
    public double speedChallengeCompleted;
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

        public void challengeAccepted(int miles, Double speed){
            this.status = "active";
            this.challengedMiles = miles;
            this.challengedSpeed = speed;
        }

    public String getId(){return this.id;}
    public void setChallengedMiles(double m){ this.challengedMiles = m;}
    public void setChallengedSpeed(double s){ this.challengedSpeed = s;}
    public void setPoints(int p){this.points = p;}
    public int getPoints(){return this.points;}
    public void isWinner(Boolean result){
        if(result){
            this.result = "winner";
        }else{
            this.result = "loser";
        }
    }
    public void setCompUserLevel(int a){
        this.compUserLevel = a;
    }

//
//        public void challengeCompleted(int completedMiles, int completedSpeed){
//            this.status = "complete";
//            if(completedMiles>=this.challengedMiles){
//                this.isChallengeMet = true;
//            }else{
//                this.isChallengeMet = false;
//            }
//        }
    }
