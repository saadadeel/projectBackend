package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Run {
    String date;
    int distance;
    int time;
    int speed;
    int score;
    user user;

    public Run(user user, int d, int t){
        this.user = user;
        this.distance = d;
        this.time = t;
        this.date = getDate();
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
