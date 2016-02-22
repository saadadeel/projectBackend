package models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Races {
        public String status;
        public String result;
        public String id;
        public String competitorUsername;

        @JsonProperty("challengedMiles")
        public int challengedMiles;

        @JsonProperty("challengedTime")
        public int challengedTime;

        public Boolean isChallengeMet;

        @JsonProperty("speedChallengeCompleted")
        public int speedChallengeCompleted;

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

        public void challengeAccepted(int miles, int time){
            this.status = "active";
            this.challengedMiles = miles;
            this.challengedTime = time;
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
