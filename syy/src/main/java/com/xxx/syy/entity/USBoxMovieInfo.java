package com.xxx.syy.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gaoruochen on 18-5-10.
 */

public class USBoxMovieInfo {



    public String date;
    public String title;
    public List<SubjectsBean> subjects;

    public static class SubjectsBean {

        public int box;
        @SerializedName("new")
        public boolean newX;
        public int rank;
        public Subjects subject;

    }
}
