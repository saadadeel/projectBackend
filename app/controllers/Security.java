package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.libs.Json;
import play.mvc.*;
import views.html.index;

import java.util.ArrayList;
import java.util.Arrays;

/**
* Created by saadadeel on 08/02/2016.
*/

public class Security extends Controller {

    public Result Authenticate() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            String username = json.findPath("username").toString();
            String password = json.findPath("password").toString();

            DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
            Jongo jongo = new Jongo(dbc);

            MongoCollection users = jongo.getCollection("users");
            user one = users.findOne("{'username':" + username + "}").as(user.class);

            //password = password.substring(1, password.length()-1);
            password = password.replaceAll("^\"|\"$", "");

            if(one.getPassword().equals(password)) {
                return ok(Json.toJson(one));
            } else {
                return badRequest("username password do not match" + one.getPassword() + " " + one.getFirstName() + " " + password);
            }
        }
    }

    public Result test(){
        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'username':'saadadeel'}").as(user.class);

        if(one.getPassword().equals("hello")) {
            return ok(Json.toJson(one));
        } else {
            return ok("username password do not match");
        }
    }

    public Result newAuthenticate(String args){

        String[] params = args.split(":");
        String username = params[0].replace("\"","");
        String password = params[1].replace("\"","");

        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'username':'" + username + "'}").as(user.class);

        password = password.replaceAll("^\"|\"$", "");

        if(one.getPassword().equals(password)) {
            return ok(Json.toJson(one));
        } else {
            return badRequest("no match");
        }
    }
}
