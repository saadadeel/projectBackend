package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.RaceReferre;
import models.Races;
import models.Run;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by saadadeel on 21/02/2016.
 */
public class Activities extends Controller {
    DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
    Jongo jongo = new Jongo(dbc);
    MongoCollection users = jongo.getCollection("users");

    public Result acceptAndCompleteRace(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            Races oneRace = new Gson().fromJson(String.valueOf(json), Races.class);

            String id= oneRace.getId();
            String compUsername = oneRace.competitorUsername;

            user two = users.findOne("{'username':" + oneRace.competitorUsername + "}").as(user.class);
            Races twoRace = two.findRace(id);

            user one = users.findOne("{'username':" + twoRace.competitorUsername + "}").as(user.class);

            if(twoRace.isComplete!=false){
                RaceReferre wow = new RaceReferre(one, two);
                wow.challengeComplete(oneRace,twoRace);
                users.update("{'username':" + one.getUsername() + "}").with(wow.getChallenger());
                users.update("{'username':" + two.getUsername() + "}").with(wow.getChallenge());
            }else{
                users.update("{'username':" + one.getUsername() + "}").with(one);
            }
            return ok(Json.toJson(one));
        }
    }

    public Result setRace(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String id= json.findPath("id").toString();//UUID.randomUUID().toString();
            String compUsername = json.findPath("compUsername").toString();//"rTT";
            String username = json.findPath("username").toString();//"rT14";

            user one = users.findOne("{'username':" + username + "}").as(user.class);
            user two = users.findOne("{'username':" + compUsername+ "}").as(user.class);

            System.out.println("Users set : " + one.getFirstName() + "   " + two.getFirstName());

            RaceReferre referre = new RaceReferre(one,two);
            referre.setChallenge();

            Races challenged = new Races(id, username, "recieved");
            challenged.setChallengedMiles(referre.getChallengedMiles());
            challenged.setChallengedSpeed(referre.getChallengedSpeed());
            challenged.setPoints(referre.getChallengedPoints());
            challenged.setCompUserLevel(one.getUserLevel());

            two.addRace(challenged);
            users.update("{'username':'" + two.getUsername() + "'}").with(two);

            Races challenger = new Races(id, compUsername, "pending");
            challenger.setChallengedMiles(referre.getChallengerMiles());
            challenger.setChallengedSpeed(referre.getChallengerSpeed());
            challenger.setPoints(referre.getChallengerPoints());
            challenger.setCompUserLevel(two.getUserLevel());

            one.addRace(challenger);
            users.update("{'username':'" + one.getUsername() + "'}").with(one);

            return ok(Json.toJson(two));
        }
    }

    public Result racePortionCompleted(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
//            String id= json.findPath("id").toString();
//            String compUsername = json.findPath("compUsername").toString();
//            String username = json.findPath("username").toString();
//            int timeDone;

            Races race= new Gson().fromJson(String.valueOf(json), Races.class);
            Races compRace = null;
            user u = null;
            user competitor = users.findOne("{'username':'" + race.competitorUsername + "'}").as(user.class);

            for(Races r: competitor.races){
                if(r.id.equals(race.getId())){
                    compRace = r;
                }
            }
            if(compRace!=null){
                u = users.findOne("{'username':'" + compRace.competitorUsername + "'}").as(user.class);
                u.updateRaces(race);
            }else{
                return ok(Json.toJson("no race"));
            }
            if(compRace.isComplete){
                RaceReferre referre = new RaceReferre(u, competitor);
                referre.challengeComplete(race,compRace);
                u = referre.getChallenger();
                competitor = referre.getChallenge();
            }else{
                compRace.status = "active";
                competitor.updateRaces(compRace);
            }
            users.update("{'username':'" + u.getUsername()+ "'}").with(u);
            users.update("{'username':'" + competitor.getUsername()+ "'}").with(competitor);

            return ok(Json.toJson(competitor));
        }
    }

    public Result recordRun(){
//        JsonNode json = request().body().asJson();
//        if(json == null) {
//            return badRequest("Expecting Json data");
//        } else {
//            String username = json.findPath("username").toString();
//            int distance = json.findPath("distance").asInt();
//            int time = json.findPath("time").asInt();
//
//            user one = users.findOne("{'username':" +] username + "}").as(user.class);
//            Run run = new Run(distance, time);
//            one.addRun(run);
//            run.setScore(one);
//            one.updateScore();
//
//            one.addRun(run);
//            users.update("{'username':" + username + "}").with(one);

            /////write challenges to users and persist///

        JsonNode json = request().body().asJson();
        if(json == null) {
            return ok("Expecting Json data");
        } else {
            DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
            Jongo jongo = new Jongo(dbc);
            MongoCollection users = jongo.getCollection("users");

            Run run= new Gson().fromJson(String.valueOf(json), Run.class);
            user one = users.findOne("{'username':'" + run.getUsername() + "'}").as(user.class);
            one.addRun(run);
            users.update("{'username':'" + run.getUsername()+ "'}").with(one);

            return ok(Json.toJson("cooool"));
        }
    }

    public Result runTest(){

        user one = users.findOne("{'username':'rTest'}").as(user.class);
        one.addRun(new Run(14000.00, 6.778, "rTest"));

        users.update("{'username':'rTest'}").with(one);
        /////write challenges to users and persist///

        return ok(Json.toJson(one));
    }
}