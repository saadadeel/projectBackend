package actions;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.libs.F;
import play.mvc.*;

import java.io.IOException;

public class ActionAuthenticator extends Action.Simple {
    private static final String AUTHORIZATION = "Authorization";
    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String REALM = "Basic realm=\"Your Realm Here\"";

    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        String token = getTokenFromHeader(ctx);
        String username = username(ctx);

        if (token != null) {
            DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
            Jongo jongo = new Jongo(dbc);
            MongoCollection users = jongo.getCollection("users");
            user user = users.findOne("{'username':'" + username + "'}").as(user.class);
            if (user != null && user.password.equals(token)) {
                ctx.request().setUsername(user.username);
                return delegate.call(ctx);
            }
        }
        Result unauthorized = Results.unauthorized("unauthorized");
        return F.Promise.pure(unauthorized);
    }

    private String getTokenFromHeader(Http.Context ctx) throws IOException {
        String authHeader = ctx.request().getHeader(AUTHORIZATION);
        if (authHeader == null) {
            ctx.response().setHeader(WWW_AUTHENTICATE, REALM);
            return null;
        }

        String auth = authHeader.substring(6);
        byte[] decodedAuth = new sun.misc.BASE64Decoder().decodeBuffer(auth);
        String[] credString = new String(decodedAuth, "UTF-8").split(":");

        if (credString == null || credString.length != 2) {
            return null;
        }

        return credString[1];
    }

    private String username(Http.Context ctx) throws IOException {
        String authHeader = ctx.request().getHeader(AUTHORIZATION);
        if (authHeader == null) {
            ctx.response().setHeader(WWW_AUTHENTICATE, REALM);
            return null;
        }

        String auth = authHeader.substring(6);
        byte[] decodedAuth = new sun.misc.BASE64Decoder().decodeBuffer(auth);
        String[] credString = new String(decodedAuth, "UTF-8").split(":");

        if (credString == null || credString.length != 2) {
            return null;
        }

        return credString[0];
    }
}
