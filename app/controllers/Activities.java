package controllers;

import actions.ActionAuthenticator;
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
import play.mvc.With;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by saadadeel on 21/02/2016.
 */

@With(ActionAuthenticator.class)
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

    public Result setRace(String uName){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String id= json.findPath("id").toString().replace("\"", "");//UUID.randomUUID().toString();
            String compUsername = json.findPath("compUsername").toString().replace("\"", "");//"rTT";
            String username = uName;//"rT14";

            user one = users.findOne("{'username':'" + username + "'}").as(user.class);
            user two = users.findOne("{'username':'" + compUsername+ "'}").as(user.class);

            RaceReferre referre = new RaceReferre(one,two);
            referre.setChallenge();

            Races challenged = new Races(id, username, "recieved");

            challenged.setChallengedDist(referre.getChallengedMiles());
            challenged.setChallengedSpeed(referre.getChallengedSpeed());
            challenged.setPoints(referre.getChallengedPoints());
            challenged.setCompUserLevel(one.getUserLevel());

            two.addRace(challenged);
            users.update("{'username':'" + two.getUsername() + "'}").with(two);

            Races challenger = new Races(id, compUsername, "pending");
            challenger.setChallengedDist(referre.getChallengerMiles());
            challenger.setChallengedSpeed(referre.getChallengerSpeed());
            challenger.setPoints(referre.getChallengerPoints());
            challenger.setCompUserLevel(two.getUserLevel());

            one.addRace(challenger);
            users.update("{'username':'" + one.getUsername() + "'}").with(one);

            return ok(Json.toJson(two));
        }
    }

    public Result deleteRace(String uName){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String id= json.findPath("id").toString().replace("\"", "");//UUID.randomUUID().toString();
            String compUsername = json.findPath("compUsername").toString().replace("\"", "");//"rTT";
            String username = uName;//json.findPath("username").toString().replace("\"", "");//"rT14";

            user one = users.findOne("{'username':'" + username + "'}").as(user.class);
            one.deleteRace(id);
            users.update("{'username':'" + one.getUsername() + "'}").with(one);

            user two = users.findOne("{'username':'" + compUsername+ "'}").as(user.class);
            two.deleteRace(id);
            users.update("{'username':'" + two.getUsername() + "'}").with(two);

            return ok(Json.toJson("deleted"));
        }
    }

    public Result racePortionCompleted(String uName){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
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
                u =  users.findOne("{'username':'" + uName + "'}").as(user.class);
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
                race.result = "Competitor to complete race";
                u.updateRaces(race);
            }
            users.update("{'username':'" + u.getUsername()+ "'}").with(u);
            users.update("{'username':'" + competitor.getUsername()+ "'}").with(competitor);

            return ok(Json.toJson(competitor));
        }
    }

    public Result recordRun(String username){
        JsonNode json = request().body().asJson();
        if(json == null) {
            return ok("Expecting Json data");
        } else {

            user one = users.findOne("{'username':'" + username + "'}").as(user.class);
            Run run= new Gson().fromJson(String.valueOf(json), Run.class);
            one.addRun(run);

            users.update("{'username':'" + username + "'}").with(one);
            return ok(Json.toJson("run added"));
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