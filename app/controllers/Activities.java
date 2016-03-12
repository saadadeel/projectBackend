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
            String id= json.findPath("id").toString();
            String compUsername = json.findPath("compUsername").toString();
            String username = json.findPath("username").toString();

            user one = users.findOne("{'username':" + username + "}").as(user.class);
            user two = users.findOne("{'username':" + compUsername+ "}").as(user.class);

            RaceReferre referre = new RaceReferre(one,two);
            referre.setChallenge();

            Races challenged = new Races(id, username);
            challenged.setChallengedMiles(referre.getChallengedMiles());
            challenged.setChallengedSpeed(referre.getChallengedSpeed());
            challenged.setPoints(referre.getChallengedPoints());

            two.addRace(challenged);
            users.update("{'username':'" + two.getUsername() + "'}").with(two);

            Races challenger = new Races(compUsername);
            challenger.setChallengedMiles(referre.getChallengerMiles());
            challenger.setChallengedSpeed(referre.getChallengerSpeed());
            challenger.setPoints(referre.getChallengerPoints());

            one.addRace(challenger);
            users.update("{'username':'" + two.getUsername() + "'}").with(one);

            /////write challenges to users and persist///

            return ok(Json.toJson(one));
        }
    }

    public Result racePortionCompleted(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String id= json.findPath("id").toString();
            String compUsername = json.findPath("compUsername").toString();
            String username = json.findPath("username").toString();
            int timeDone;

            user one = users.findOne("{'username':" + username + "}").as(user.class);
            user two = users.findOne("{'username':" + compUsername+ "}").as(user.class);

            RaceReferre referre = new RaceReferre(one, two);
            referre.setChallenge();

            /////write challenges to users and persist///

            return ok(Json.toJson("okk"));
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
        user u = new user("Ammar", "amRaufi44", "Raufi", "password");
        String username = u.getUsername();
        String cu = "ShameelahK";

        user one = users.findOne("{'username':\"" + username + "\"}").as(user.class);
        one.acceptRace(cu);

        users.update("{'username':\"" + username + "\"}").with(one);
        /////write challenges to users and persist///

        return ok(Json.toJson(one));
    }
}