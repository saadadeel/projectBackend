package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Run {
    public String date;
    public double distance;
    public int time;
    public double speed;
    public int score = 3;
    public String username;

    public Run(){}

    public Run(double d, int t){
        this.distance = d;
        this.time = t;
        this.speed = d/t;
        this.date = getDate();
    }

    public Run(Double d, Double speed, String u){
        this.distance = d;
        this.speed = speed;
        this.time = (int)(d/speed);
        this.username = u;
    }

    public double getDistance(){
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public int getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getUsername(){
        return this.username;
    }

    public void setScore(user user) {
        int temp = 0;

        ArrayList<Run> userRuns = user.getRuns();
        ArrayList<Double> dist = new ArrayList<Double>();
        ArrayList<Double> speed = new ArrayList<Double>();

        setBasicPoints(user);
        setBonusPoints(userRuns);
    }

    private void setBasicPoints(user user){
        if(user.getAverageDistance()< this.getDistance() && user.getAverageSpeed()<this.getSpeed()){
            this.score += 3;
        }
    }

    private void setBonusPoints(ArrayList<Run> userRuns){
        int counter = 0;
        int numOfRuns = userRuns.size();
        if (numOfRuns < 5) {
            counter = numOfRuns;
        } else {
            counter = 5;
        }
        if (userRuns != null && userRuns.size() > 2) {
            for (int i = 0; i < counter; i++) {
                if (userRuns.get(i).getDistance() > userRuns.get(i + 1).getDistance() && userRuns.get(i).getSpeed() < userRuns.get(i + 1).getTime()) {
                    System.out.println("/////// here");
                    System.out.println(this.score);
                    this.score += 4;
                    if (i == 4) {
                        this.score += 10;
                    }
                } else {
                    System.out.println("******* yessss what");
                    break;
                }
            }
        }
    }
}
