package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.RaceReferre;
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

    public Result setRace(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String id= json.findPath("id").toString();
            String compUsername = json.findPath("compUsername").toString();
            String username = json.findPath("username").toString();

            user one = users.findOne("{'username':" + username + "}").as(user.class);
            one.setRace(compUsername);
            users.update("{'username':" + username + "}").with(one);

            user two = users.findOne("{'username':" + compUsername+ "}").as(user.class);
            two.challengeRecieved(id, username);
            users.update("{'username':" + compUsername+ "}").with(two);


            return ok(Json.toJson("okk"));
            }
    }

    public Result acceptRace(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String id= json.findPath("id").toString();
            String compUsername = json.findPath("compUsername").toString();
            String username = json.findPath("username").toString();

            user one = users.findOne("{'username':" + username + "}").as(user.class);
            user two = users.findOne("{'username':" + compUsername+ "}").as(user.class);

            RaceReferre referre = new RaceReferre(one, two);
            referre.setChallenge();
            ArrayList<int[]> challenges = referre.getChallenges();

            /////write challenges to users and persist///

            return ok(Json.toJson("okk"));
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
            ArrayList<int[]> challenges = referre.getChallenges();

            /////write challenges to users and persist///

            return ok(Json.toJson("okk"));
        }
    }

    public Result recordRun(){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String username = json.findPath("username").toString();
            int distance = json.findPath("distance").asInt();
            int time = json.findPath("time").asInt();

            user one = users.findOne("{'username':" + username + "}").as(user.class);
            Run run = new Run(distance, time);
            one.addRun(run);
            run.setScore(one);
            one.updateScore();

            one.addRun(run);
            users.update("{'username':" + username + "}").with(one);

            /////write challenges to users and persist///

            return ok(Json.toJson("okk"));
        }
    }

    public Result runTest(){
        user u = new user("Ammar", "amRaufi44", "Raufi", "password");
        String username = u.getUsername();
        String cu = "HassanHK";

        user one = users.findOne("{'username':\"" + username + "\"}").as(user.class);
        one.acceptRace(cu);

        users.update("{'username':\"" + username + "\"}").with(one);
        /////write challenges to users and persist///

        return ok(Json.toJson(one));
    }
}
