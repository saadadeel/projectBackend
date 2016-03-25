package models;

/**
 * Created by saadadeel on 07/03/2016.
 */
public class minimalUser implements Comparable{
    public String username;
    public double averageDistance = 0.0;
    public double averageSpeed = 0.0;
    public int userScore = 0;
    public int userLevel;

    public minimalUser(){}

    public int getUserScore(){
        return this.userScore;
    }

    @Override
    public int compareTo(Object o) {
        if(o!=null) {
            int score = ((minimalUser) o).getUserScore();
            return score - getUserScore();
        }else{
            int score = 0;
            return score - getUserScore();
        }
    }
}
