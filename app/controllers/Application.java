package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.mongodb.DB;
import models.*;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.mvc.*;

import views.html.*;
import com.mongodb.MongoClient;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import play.libs.Json;

import static com.mongodb.client.model.Sorts.ascending;

public class Application extends Controller {
    DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
    Jongo jongo = new Jongo(dbc);
    MongoCollection users = jongo.getCollection("users");
    MongoCollection levelCollection = jongo.getCollection("userByLevel");

    public Result index() {
        return ok(index.render("Saad Adeel"));
    }

    public Result saad() {
        return ok(index.render("Saad Adeeeeeeel!!!!"));
    }

    public Result mongoTest() throws ParseException, IOException {
        DB db = new MongoClient("localhost", 4800).getDB("competifit");
        Jongo jongo = new Jongo(db);

        MongoCollection users = jongo.getCollection("users");
        //user one = users.findOne("{'name': 'saad'}").as(user.class);
        //user tester = new user("saad", "habhbk");
        //users.save(tester);

        return ok(Json.toJson("yes"));
    }

    public Result serverTest() throws ParseException, IOException {

        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);

        MongoCollection users = jongo.getCollection("users");
        //user one = users.findOne("{'First Name': 'Saad'}").as(user.class);
        user tester = new user("Adam", "aDriver", "Driver", "password");

        users.save(tester);

        return ok(Json.toJson(tester));
    }

    public Result userDetails(String uName) throws ParseException, IOException {

        ArrayList<String> leagueUsernames = new ArrayList<String>();
        ArrayList<minimalUser> league = new ArrayList<minimalUser>();

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);
        leagueUsernames = one.getLeagueUsernames();

        for(String un: leagueUsernames){
            minimalUser mOne = users.findOne("{'username':'" + un + "'}").as(minimalUser.class);
            league.add(mOne);
        }
        Collections.sort(league);
        one.setleague(league);
        return ok(Json.toJson(one));
    }


    public Result signIn() {
        JsonNode json = request().body().asJson();
//        ArrayList<minimalUser> mU= new ArrayList<minimalUser>();
        ArrayList<String> randomUsernames = new ArrayList<String>();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            user u = new Gson().fromJson(String.valueOf(json), user.class);
            Level level = levelCollection.findOne("{'level':" + u.getUserLevel() + "}").as(Level.class);
            level.addUsername(u.getUsername());
            users.save(u);

            //////Set up league/////

            if(u.getUserLevel()<=2) {
                level = levelCollection.findOne("{'level':" + (u.getUserLevel() - 1) + "}").as(Level.class);
                randomUsernames = level.findRandom(6);
                for (int i = 0; i < randomUsernames.size(); i++) {
                    String randomU = randomUsernames.get(i);
                    u.addLeagueUsernames(randomU);
                }
                level = levelCollection.findOne("{'level':" + (u.getUserLevel() + 1) + "}").as(Level.class);
                randomUsernames = level.findRandom(3);
                for (int i = 0; i < randomUsernames.size(); i++) {
                    String randomU = randomUsernames.get(i);
                    u.addLeagueUsernames(randomU);
                }
            }else{
                level = levelCollection.findOne("{'level':'" + (u.getUserLevel()) + "'}").as(Level.class);
                randomUsernames = level.findRandom(9);
                for(int i = 0; i < randomUsernames.size(); i++){
                    String randomU = randomUsernames.get(i);
                    u.addLeagueUsernames(randomU);
                }
                level = levelCollection.findOne("{'level':'" + (u.getUserLevel() + 1) + "'}").as(Level.class);
                randomUsernames = level.findRandom(3);
                for(int i = 0; i < randomUsernames.size(); i++){
                    String randomU = randomUsernames.get(i);
                    u.addLeagueUsernames(randomU);
                }
            }

            users.update("{'username':'" + u.getUsername()+ "'}").with(u);
            return ok(Json.toJson(u));
        }
    }

    public Result addTestUser() {
        user u = new user("test", "race3", "test", "password");
//        Level level = levelCollection.findOne("{'level':" + u.getUserLevel() + "}").as(Level.class);
//        level.addUsername(u.getUsername());
//        levelCollection.update("{'level':" + u.getUserLevel()+ "}").with(level);
        users.save(u);

        user one = users.findOne("{'username':'" + u.getUsername() + "'}").as(user.class);
        one.addScore(10);
        users.update("{'username':'" + u.getUsername() + "'}").with(one);
//
        return ok(Json.toJson(users.findOne("{'username':'" + u.getUsername() + "'}").as(user.class)));
    }

    public Result update(){
        ArrayList<String> leagueUsernames = new ArrayList<String>();
        ArrayList<minimalUser> league = new ArrayList<minimalUser>();
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            user dataFromClient = new Gson().fromJson(String.valueOf(json), user.class);
            user one = users.findOne("{'username':'" + dataFromClient.getUsername() + "'}").as(user.class);

           ArrayList<Run> runData = dataFromClient.getRuns();
            ArrayList<Races> races = dataFromClient.getRaces();

            users.update("{'username':'" + one.getUsername() + "'}").with(one);
            for(Races race : races){
                if(race.isComplete){
                    one.addRace(race);
                    users.update("{'username':'" + one.getUsername() + "'}").with(one);
                }
            }
            leagueUsernames = one.getLeagueUsernames();
            for(String un: leagueUsernames){
                minimalUser mOne = users.findOne("{'username':'" + un + "'}").as(minimalUser.class);
                league.add(mOne);
            }
            Collections.sort(league);
            one.setleague(league);

            return ok(Json.toJson(one));
        }
    }
}