package models;

/**
 * Created by saadadeel on 07/03/2016.
 */
public class minimalUser implements Comparable{
    public String username;
    public int averageDistance = 0;
    public Double averageSpeed = 0.0;
    public int userScore = 0;
    public int userLevel;

    public minimalUser(){}

//    public int compareTo(minimalUser comparestu) {
//        int score=((minimalUser)comparestu).getUserScore();
//        /* For Ascending order*/
////        return this.studentage-compareage;
//
//        /* For Descending order do like this */
//        return score-getUserScore();
//    }

    public int getUserScore(){
        return this.userScore;
    }

    @Override
    public int compareTo(Object o) {
        int score=((minimalUser) o).getUserScore();
        /* For Ascending order*/
//        return this.studentage-compareage;

        /* For Descending order do like this */
        return score-getUserScore();
    }
}
