package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.client.MongoCursor;
import models.UserSocketActor;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.mvc.*;

import views.html.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import play.api.libs.json.*;
import play.libs.Json;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;

import static java.util.Arrays.asList;

public class Application extends Controller {

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
        DB db = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
        Jongo jongo = new Jongo(db);

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'username':'" + uName + "'}").as(user.class);

        return ok(Json.toJson(one));
    }


    public Result signIn() {
        JsonNode json = request().body().asJson();
        if(json == null) {
            return badRequest("Expecting Json data");
        } else {
            DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
            Jongo jongo = new Jongo(dbc);
            MongoCollection users = jongo.getCollection("users");

            user u = new Gson().fromJson(String.valueOf(json), user.class);
            users.save(u);

            return ok(Json.toJson(u))
                    ;
        }
    }

    public static WebSocket<String> socket() {
        return WebSocket.withActor(UserSocketActor::props);
    }
}