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
        public int challengedMiles;

        @JsonProperty("challengedTime")
        public Double challengedSpeed;

        public Boolean isComplete;
        public int completedMiles;
        public Double completedSpeed;

        @JsonProperty("speedChallengeCompleted")
        public int speedChallengeCompleted;
        public String result;
        public int points;

        public Races(){}

        public Races(String cUN){
            this.status = "pending";
            this.competitorUsername = cUN;
        }

        public Races(String st, String cUN){
            this.status = "recieved";
            this.competitorUsername = cUN;
            this.id = st;
        }

        public void challengeAccepted(int miles, Double speed){
            this.status = "active";
            this.challengedMiles = miles;
            this.challengedSpeed = speed;
        }

    public String getId(){return this.id;}
    public void setChallengedMiles(int m){ this.challengedMiles = m;}
    public void setChallengedSpeed(Double s){ this.challengedSpeed = s;}
    public void setPoints(int p){this.points = p;}
    public int getPoints(){return this.points;}
    public void isWinner(Boolean result){
        if(result){
            this.result = "winner";
        }else{
            this.result = "loser";
        }
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
