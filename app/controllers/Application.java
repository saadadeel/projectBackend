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

        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);

        MongoCollection users = jongo.getCollection("users");
        //user one = users.findOne("{'First Name': 'Saad'}").as(user.class);
        user tester = new user("Adam", "aDriver", "Driver", "password");

        users.save(tester);

        return ok(Json.toJson(tester));
    }

    @With(ActionAuthenticator.class)
    public Result userDetails(String uName) throws ParseException, IOException {

        ArrayList<String> leagueUsernames = new ArrayList<String>();
        ArrayList<minimalUser> league = new ArrayList<minimalUser>();
        boolean isDuplicate = false;

        MongoCollection users = jongo.getCollection("users");
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

            user u = new user(u1.getFirstName(), u1.getUsername(), u1.lastName, u1.getPassword());
            u.setUserLevel(u1.getUserLevel());
            u.averageDistance = u1.getAverageDistance();
            u.averageSpeed = u1.getAverageSpeed();

            user one = users.findOne("{'username':'" + u.getUsername() + "'}").as(user.class);
            if(one==null){
                Level level = levelCollection.findOne("{'level':" + u.getUserLevel() + "}").as(Level.class);
                level.addUsername(u.getUsername());
                levelCollection.update("{'level':" + u.getUserLevel()+ "}").with(level);
                users.save(u);
                return ok(Json.toJson(u));
            }else{
                return ok("user exists");
            }



            //////Set up league/////

//            if(u.getUserLevel()<=2) {
//                level = levelCollection.findOne("{'level':" + (u.getUserLevel() - 1) + "}").as(Level.class);
//                randomUsernames = level.findRandom(6);
//                for (int i = 0; i < randomUsernames.size(); i++) {
//                    String randomU = randomUsernames.get(i);
//                    u.addLeagueUsernames(randomU);
//                }
//                level = levelCollection.findOne("{'level':" + (u.getUserLevel() + 1) + "}").as(Level.class);
//                randomUsernames = level.findRandom(3);
//                for (int i = 0; i < randomUsernames.size(); i++) {
//                    String randomU = randomUsernames.get(i);
//                    u.addLeagueUsernames(randomU);
//                }
//            }else{
//                level = levelCollection.findOne("{'level':'" + (u.getUserLevel()) + "'}").as(Level.class);
//                randomUsernames = level.findRandom(9);
//                for(int i = 0; i < randomUsernames.size(); i++){
//                    String randomU = randomUsernames.get(i);
//                    u.addLeagueUsernames(randomU);
//                }
//                level = levelCollection.findOne("{'level':'" + (u.getUserLevel() + 1) + "'}").as(Level.class);
//                randomUsernames = level.findRandom(3);
//                for(int i = 0; i < randomUsernames.size(); i++){
//                    String randomU = randomUsernames.get(i);
//                    u.addLeagueUsernames(randomU);
//                }
//            }
//            u.addLeagueUsernames(u.getUsername());

//            users.update("{'username':'" + u.getUsername()+ "'}").with(u);
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

    public Result update(){
//        ArrayList<String> leagueUsernames = new ArrayList<String>();
//        ArrayList<minimalUser> league = new ArrayList<minimalUser>();
//        JsonNode json = request().body().asJson();
//        if(json == null) {
//            return badRequest("Expecting Json data");
//        } else {
//            user dataFromClient = new Gson().fromJson(String.valueOf(json), user.class);
//            user one = users.findOne("{'username':'" + dataFromClient.getUsername() + "'}").as(user.class);
//
//           ArrayList<Run> runData = dataFromClient.getRuns();
//            ArrayList<Races> races = dataFromClient.getRaces();
//
//            users.update("{'username':'" + one.getUsername() + "'}").with(one);
//            for(Races race : races){
//                if(race.isComplete){
//                    one.addRace(race);
//                    users.update("{'username':'" + one.getUsername() + "'}").with(one);
//                }
//            }
//            leagueUsernames = one.getLeagueUsernames();
//            for(String un: leagueUsernames){
//                minimalUser mOne = users.findOne("{'username':'" + un + "'}").as(minimalUser.class);
//                league.add(mOne);
//            }
//            Collections.sort(league);
//            one.setleague(league);
//
//            return ok(Json.toJson(one));
//        }
        return ok("hello");
    }

//   public Result socket(String uName) {
//
//       user oldUser = getUser(uName);
//
//       TimerTask secondCounter = new TimerTask() {
//           @Override
//           public void run() {
//               user user = getUser(uName);
//                if(user.getRaces() == oldUser.getRaces() && user.getLeague() == oldUser.getLeague()){
//                    System.out.println("HELLOOOO");
//                }else{
//                    System.out.println("WHATTT");
//                    gotUser = true;
//                }
//
//           }
//       };
//       timer.scheduleAtFixedRate(secondCounter, 10 * 1000, 10 * 1000);
//
//       return ok(Json.toJson(getUser(uName)));
//   }
//
//    public user getUser(String uName){
//        ArrayList<String> leagueUsernames = new ArrayList<String>();
//        ArrayList<minimalUser> league = new ArrayList<minimalUser>();
//
//        user user = new user();
//        user oldUser = new user();
//
//        boolean isDuplicate = false;
//
//        MongoCollection users = jongo.getCollection("users");
//        user = users.findOne("{'username':'" + uName + "'}").as(user.class);
//
//        Level userLevel = levelCollection.findOne("{'level':" + (user.getUserLevel()) + "}").as(Level.class);
//        leagueUsernames = userLevel.getUsernames();
//
//        for(String un: leagueUsernames){
//            minimalUser mOne = users.findOne("{'username':'" + un + "'}").as(minimalUser.class);
//            if(mOne!=null){
//                league.add(mOne);
//            }
//        }
//        Collections.sort(league);
//        user.setleague(league);
//        return user;
//    }

}