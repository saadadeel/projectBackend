package controllers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import models.UserSocketActor;
import org.jetbrains.annotations.NotNull;
import play.libs.F;
import play.mvc.*;

import play.mvc.WebSocket;

import javax.inject.Inject;

/**
 * Created by saadadeel on 29/02/2016.
 */
public class UserSockets extends Controller {

    @NotNull
    public static WebSocket<String> getUser() {
//        return WebSocket.withActor(UserSocketActor::props);

//        return WebSocket.withActor(new F.Function<ActorRef, Props>() {
//            public Props apply(ActorRef out) throws Throwable {
//                return UserSocketActor.props(out);
//            }
//        });
//    }

        return new WebSocket<String>() {
            public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
                out.write("Hello!");
                out.close();
            }
        };
    }
}
