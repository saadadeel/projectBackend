package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.DB;
import com.mongodb.client.MongoCursor;
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
        DB db = new MongoClient().getDB("tt");
        Jongo jongo = new Jongo(db);

            MongoCollection users = jongo.getCollection("users");
//        user one = users.findOne("{'First Name': 'Saad'}").as(user.class);

        return ok(Json.toJson("Yesssss"));
    }

    public Result serverTest() throws ParseException, IOException {
//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase db = mongoClient.getDatabase("test");
//        addData(db);
//        String sample = "sample";
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(sample));

//        FindIterable<Document> iterable = db.getCollection("restaurants").find(
//                new Document("address.zipcode", "10075"));
//
//        iterable.forEach(new Block<Document>() {
//            @Override
//            public void apply(final Document document) {
//                try {
//                    writer.write(document.toJson());
//                    writer.newLine();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        try {
//            for (Document doc : db.getCollection("users").find(eq("username", "saadadeel"))) {
//                writer.write(doc.toJson());
//                writer.newLine();
//                System.out.println(doc);
//            }
//        }finally {
//                writer.close();
//            }

//        MongoClient mongoClient = new MongoClient("localhost", 27017);
//        MongoDatabase dbC = mongoClient.getDatabase("db");
        //dbC.getCollection("users");

        DB dbc = new MongoClient().getDB("competifitDB");
        Jongo jongo = new Jongo(dbc);

//        MongoCollection users = jongo.getCollection("users");
//        user one = users.findOne("{'First Name': 'Saad'}").as(user.class);

        return ok(Json.toJson("yeaaaaa!!!!!"));
    }
}