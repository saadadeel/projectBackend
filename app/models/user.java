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
    public String name;
    public String username;

    public user(String  n, String un){
        this.name = n;
        this.username = un;
    }
    public user(){
    }
}
