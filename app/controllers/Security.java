package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.mvc.*;
import views.html.index;

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

            if(one.getPassword().equals(password)) {
                return ok("Hello " + username);
            } else {
                return ok("username password do not match" + one.getPassword() + " " + one.getFirstName());
            }

//            return ok("Hello " + one.getPassword());
        }
    }

    public Result test(){
        DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'username':'saadadeel'}").as(user.class);

        if(one.getPassword().equals("hello")) {
            return ok("Hello");
        } else {
            return badRequest("username password do not match");
        }
    }
}
