package com.example.greapp;

import android.util.Log;
import org.decimal4j.util.DoubleRounder;

public class  Score {

    private static final int Easy_correct = 1;
    private static final double Medium_correct = 1.5;
    private static final int Hard_correct = 2;
    private static final double Easy_incorrect = 0.8;
    private static final double Medium_incorrect = 0.6;
    private static final double Hard_incorrect = 0.5;




    private  double total_score;

    void setTotal_score(double total_score)
    {
        this.total_score = total_score>0?total_score:0;
    }

    void calculate_score(Boolean crct, String diff)
    {
        if (crct)
        {
            switch ( diff.toLowerCase()) {
                case "easy": total_score+= Easy_correct;
                    break;
                case "medium": total_score+= Medium_correct;
                    break;
                case "hard": total_score+= Hard_correct;
                    break;
            }
        }
        else
        {
            switch ( diff.toLowerCase()) {
                case "easy": total_score-= Easy_incorrect;
                    break;
                case "medium": total_score-= Medium_incorrect;
                    break;
                case "hard": total_score-= Hard_incorrect;
                    break;
            }

        }
        Log.e("new ouput of score ", Double.toString(total_score));
    }

    double getTotal_score() {
        return DoubleRounder.round(total_score, 2);
    }


}