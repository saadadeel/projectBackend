package models;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

/**
 * Created by saadadeel on 26/01/2016.
 */

public class user{

    private long key;

    public String firstName;
    public String lastName;
    public String username;
    public String password;


    public user(String fn, String un, String ln, String pw){
        this.firstName = fn;
        this.username = un;
        this.lastName = ln;
        this.password = pw;
    }

    public String getPassword(){return this.password;}
}
