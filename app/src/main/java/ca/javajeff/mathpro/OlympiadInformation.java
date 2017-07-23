package ca.javajeff.mathpro;

import android.support.constraint.solver.ArrayRow;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by Саддам on 21.07.2017.
 */

public class OlympiadInformation {
    public String[] array = {"International contests", "National and regional contests", "Undergraduate contests", "National olympiads", "Team Selection Tests", "Contests for junior students"};
    private int OlympiadType;

    public OlympiadInformation() {

    }
//    public String setType (int i) {
//        this.OlympiadType = array[i];
//    }

    public int getOlympiadType() {
        return OlympiadType;
    }

    public void setOlympiadType(int olympiadType) {
        OlympiadType = olympiadType;
    }

//    public int getOlympiadType() {
//        return OlympiadType;
//    }
//
//    public void setOlympiadType(int olympiadType) {
//        OlympiadType = array[olympiadType];
//    }
}
