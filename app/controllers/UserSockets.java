package controllers;

import akka.actor.ActorSystem;
import models.UserSocketActor;
import org.jetbrains.annotations.NotNull;
import play.mvc.*;

import play.mvc.WebSocket;

import javax.inject.Inject;

/**
 * Created by saadadeel on 29/02/2016.
 */
public class UserSockets extends Controller {

    @NotNull
    public static WebSocket<String> getUser() {
        return WebSocket.withActor(UserSocketActor::props);
    }
}
