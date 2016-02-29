package models;

import akka.actor.*;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.api.mvc.Result;
import play.libs.Json;

/**
 * Created by saadadeel on 29/02/2016.
 */

public class UserSocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(UserSocketActor.class, out);
    }

    private final ActorRef out;

    public UserSocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {

//            DB db = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
//            Jongo jongo = new Jongo(db);
//            MongoCollection users = jongo.getCollection("users");
//            user one = users.findOne("{'username':" + message + "}").as(user.class);

            out.tell("yoo", self());
//            out.tell(Json.toJson(one), self());
        }
    }
}
