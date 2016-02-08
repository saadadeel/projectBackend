package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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
            if(username == null) {
                return badRequest("Missing parameter [name]");
            } else {
                return ok("Hello " + username);
            }
        }
    }
}
