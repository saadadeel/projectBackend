package controllers;

import actions.ActionAuthenticator;
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
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

import play.libs.Json;
import akka.actor.*;
import play.libs.F.*;
import play.mvc.WebSocket;

import static com.mongodb.client.model.Sorts.ascending;


public class Application extends Controller {

    DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
    Jongo jongo = new Jongo(dbc);
    MongoCollection users = jongo.getCollection("users");
    MongoCollection levelCollection = jongo.getCollection("userByLevel");

    Timer timer = new Timer();
    boolean gotUser = false;

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

//        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
//        Jongo jongo = new Jongo(dbc);
//
//        MongoCollection users = jongo.getCollection("users");
//        //user one = users.findOne("{'First Name': 'Saad'}").as(user.class);
//        user tester = new user("Adam", "aDriver", "Driver", "password");
//
//        users.save(tester);
//
        return ok();
    }

    @With(ActionAuthenticator.class)
    public Result userDetails(String uName) throws ParseException, IOException {

        ArrayList<String> leagueUsernames = new ArrayList<String>();
        ArrayList<minimalUser> league = new ArrayList<minimalUser>();
        boolean isDuplicate = false;

        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);
        Level level = levelCollection.findOne("{'level':" + one.getUserLevel() + "}").as(Level.class);
        Level oldLevel = levelCollection.findOne("{'level':" + one.oldUserLevel + "}").as(Level.class);

        for(String duplicate:level.getUsernames()){
            if(duplicate.equals(one.getUsername())){
                isDuplicate = true;
            }
        }
        if(one.oldUserLevel!=0){
            if(!isDuplicate){
                level.addUsername(one.getUsername());
                levelCollection.update("{'level':" + one.getUserLevel()+ "}").with(level);
            }

            oldLevel.deleteUsername(one.getUsername());
            levelCollection.update("{'level':" + one.oldUserLevel+ "}").with(oldLevel);

            one.oldUserLevel = 0;
            users.update("{'username':'" + one.getUsername()+ "'}").with(one);
        }

        Level userLevel = levelCollection.findOne("{'level':" + (one.getUserLevel()) + "}").as(Level.class);
        leagueUsernames = userLevel.getUsernames();

        for(String un: leagueUsernames){
            minimalUser mOne = users.findOne("{'username':'" + un + "'}").as(minimalUser.class);
            if(mOne!=null){
                league.add(mOne);
            }
        }

        Collections.sort(league);
        one.setleague(league);
        dbc.getMongo().close();

        return ok(Json.toJson(one));
    }

    @With(ActionAuthenticator.class)
    public Result minimalUserDetails(String uName) throws ParseException, IOException {

        MongoCollection users = jongo.getCollection("users");
        minimalUser one = users.findOne("{'username':'" + uName + "'}").as(minimalUser.class);

        if(one!=null){
            return ok(Json.toJson(one));
        }
        else{
            return badRequest("No User Found");
        }
    }

    public Result signIn() {
        JsonNode json = request().body().asJson();
        ArrayList<String> randomUsernames = new ArrayList<String>();

        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            user u1 = new Gson().fromJson(String.valueOf(json), user.class);

            user u = new user(u1.getFirstName(), u1.getUsername(), u1.lastName, u1.getPassword(),u1.getUserLevel());

            user one = users.findOne("{'username':'" + u.getUsername() + "'}").as(user.class);
            if(one==null){

                Level level = levelCollection.findOne("{'level':" + u.getUserLevel() + "}").as(Level.class);
                level.addUsername(u.getUsername());
                levelCollection.update("{'level':" + u.getUserLevel()+ "}").with(level);
                users.save(u);
                return ok(Json.toJson(u));

            }else{
                return badRequest("user exists");
            }
        }
    }

    public Result addTestUser() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("steve");
        names.add("chris");
        names.add("michael");
        names.add("daniel");
        names.add("henry");
        names.add("usainBolt");
        names.add("thom");
        names.add("jonny");
        names.add("ed");
        names.add("phil");
        names.add("colin");
        names.add("chiara");
        names.add("runner");
        names.add("compete");


//        Level l = new Level(4);
//        levelCollection.save(l);
//
//            for (int i = 0; i < names.size(); i++) {
//                user u = new user("firstName", names.get(i) + 4, "lastName", "password");
//                Run run = new Run(4000.0, 2.778, u.getUsername());
//                u.addRun(run);
//
//                Level level = levelCollection.findOne("{'level':4}").as(Level.class);
//                level.addUsername(u.getUsername());
//                levelCollection.update("{'level':4}").with(level);
//            }

        return ok(Json.toJson(levelCollection.findOne("{'level':1}").as(Level.class)));
    }

    @With(ActionAuthenticator.class)
    public Result userRuns(String uName) throws ParseException, IOException {
        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);

        if(one!=null){
            ArrayList<Run> runs = one.getRuns();
            return ok(Json.toJson(runs));
        }
        else{
            return badRequest("No User Found");
        }
    }

    @With(ActionAuthenticator.class)
    public Result userRaces(String uName) throws ParseException, IOException {
        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);

        if(one!=null){
            ArrayList<Races> races = one.getRaces();
            return ok(Json.toJson(races));
        }
        else{
            return badRequest("No User Found");
        }
    }

    @With(ActionAuthenticator.class)
    public Result userSingleRace(String uName, String raceID) throws ParseException, IOException {
        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);
        if(one!=null){
            ArrayList<Races> races = one.getRaces();
            for(Races race: races){
                if(race.getId().equals(raceID.trim())){
                    return ok(Json.toJson(race));
                }
            }
            return badRequest("No Race Found");
        }
        else{
            return badRequest("No User Found");
        }
    }

    @With(ActionAuthenticator.class)
    public Result userLeague(String uName) throws ParseException, IOException {
        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);
        Level userLevel = levelCollection.findOne("{'level':" + (one.getUserLevel()) + "}").as(Level.class);
        ArrayList<minimalUser> league = new ArrayList<minimalUser>();

        if(one!=null){
            ArrayList<String> leagueUsernames = userLevel.getUsernames();
            for(String un: leagueUsernames){
                minimalUser mOne = users.findOne("{'username':'" + un + "'}").as(minimalUser.class);
                if(mOne!=null){
                    league.add(mOne);
                }
            }
            return ok(Json.toJson(league));
        }
        else{
            return badRequest("No User Found");
        }
    }

    public Result changePassword() {
        JsonNode json = request().body().asJson();
        String username = "";
        String newPassword = "";

        if (json == null) {
            return badRequest("Expecting Json data");
        } else {
            username = json.findPath("username").toString().replace("\"", "");
            newPassword = json.findPath("password").toString().replace("\"", "");
        }

        user one = users.findOne("{'username':'" + username + "'}").as(user.class);
        if (one != null) {
            one.password = newPassword;
            users.update("{'username':'" + username + "'}").with(one);
            return ok();
        } else {
            return badRequest("No User Found");
        }
    }
}