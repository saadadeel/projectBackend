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

    public void addData(MongoDatabase db) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        db.getCollection("restaurants").insertOne(
                new Document("address",
                        new Document()
                                .append("street", "2 Avenue")
                                .append("zipcode", "10075")
                                .append("building", "1480")
                                .append("coord", asList(-73.9557413, 40.7720266)))
                        .append("borough", "Manhattan")
                        .append("cuisine", "Italian")
                        .append("grades", asList(
                                new Document()
                                        .append("date", format.parse("2014-10-01T00:00:00Z"))
                                        .append("grade", "A")
                                        .append("score", 11),
                                new Document()
                                        .append("date", format.parse("2014-01-16T00:00:00Z"))
                                        .append("grade", "B")
                                        .append("score", 17)))
                        .append("name", "Vella")
                        .append("restaurant_id", "41704620"));
    }

    public Result mongoTest() throws ParseException, IOException {
        DB db = new MongoClient().getDB("test");
        Jongo jongo = new Jongo(db);

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'First Name': 'Saad'}").as(user.class);

        return ok(Json.toJson(one));
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

        DB db = new MongoClient().getDB("competifitDB");
        Jongo jongo = new Jongo(db);

        MongoCollection users = jongo.getCollection("users");
        user one = users.findOne("{'First Name': 'Saad'}").as(user.class);

        return ok(Json.toJson(one));
    }


}