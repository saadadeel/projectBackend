package actions;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import models.user;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;

//public class BasicAuthAction extends Action<BasicAuthAction.Authenticator> {
//
//    private static final String AUTHORIZATION = "authorization";
//    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
//    private static final String REALM = "Basic realm=\"Your Realm Here\"";

//    @Override
//    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
//
//        try {
//            Authenticator authenticator = configuration.value().newInstance();
//            String username = authenticator.getUsername(ctx);
//            if(username == null) {
//                Result unauthorized = authenticator.onUnauthorized(ctx);
//                return F.Promise.pure(unauthorized);
//            } else {
//                try {
//                    ctx.request().setUsername(username);
//                    return delegate.call(ctx);
//                } finally {
//                    ctx.request().setUsername(null);
//                }
//            }
//        } catch(RuntimeException e) {
//            throw e;
//        } catch(Throwable t) {
//            throw new RuntimeException(t);
//        }
//    }
//    class Authenticator extends Results {
//
//        public String getUsername(Http.Context context) throws Throwable {
//            String authHeader = context.request().getHeader(AUTHORIZATION);
//            if (authHeader == null) {
//                context.response().setHeader(WWW_AUTHENTICATE, REALM);
//                return unauthorized().toString();
//            }
//            String auth = authHeader.substring(6);
//            byte[] decodedAuth = new sun.misc.BASE64Decoder().decodeBuffer(auth);
//            String[] credString = new String(decodedAuth, "UTF-8").split(":");
//
//            if (credString == null || credString.length != 2) {
//                return unauthorized().toString();
//            }
//
//            String username = credString[0];
//            String password = credString[1];
//
//            DB dbc = new MongoClient("178.62.68.172", 27017).getDB("competifitDB");
//            Jongo jongo = new Jongo(dbc);
//            MongoCollection users = jongo.getCollection("users");
//            user one = users.findOne("{'username':" + username + "}").as(user.class);
//
//            password = password.replaceAll("^\"|\"$", "");
//            user authUser;
//
//            if(one.getPassword().equals(password)) {
//                authUser = one;
//            } else {
//                return unauthorized().toString();
//            }
//
//            return (authUser == null) ? unauthorized().toString(): delegate.call(context).toString();
//        }
//        public Result onUnauthorized(Http.Context ctx) {
//            return unauthorized(views.html.defaultpages.unauthorized.render());
//        }
//
//    }
//}
