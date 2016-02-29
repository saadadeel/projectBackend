package controllers;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import controllers.Actors.UserSocketActor;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import akka.actor.*;
import play.libs.F.*;
import play.mvc.WebSocket;

/**
 * Created by saadadeel on 29/02/2016.
 */
public class UserSockets extends Controller {

    public static WebSocket<String> getUser() {
        return WebSocket.withActor(UserSocketActor::props);
    }
}
