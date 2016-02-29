package controllers;

import akka.actor.ActorSystem;
import models.UserSocketActor;
import play.mvc.*;

import play.mvc.WebSocket;

import javax.inject.Inject;

/**
 * Created by saadadeel on 29/02/2016.
 */
public class UserSockets extends Controller {

    public WebSocket getUser() {
        return WebSocket.withActor(UserSocketActor::props);
    }
}
