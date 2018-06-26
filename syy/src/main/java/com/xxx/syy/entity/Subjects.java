package com.xxx.syy.entity;

import java.util.List;

/**
 * Created by gaoruochen on 18-4-13.
 */

public class Subjects {


    /**
     * rating : {"max":10,"average":9.6,"stars":"50","min":0}
     * genres : ["犯罪","剧情"]
     * title : 肖申克的救赎
     * casts : [{"alt":"https://movie.douban.com/celebrity/1054521/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p17525.webp","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p17525.webp","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p17525.webp"},"name":"蒂姆·罗宾斯","id":"1054521"},{"alt":"https://movie.douban.com/celebrity/1054534/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34642.webp","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34642.webp","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p34642.webp"},"name":"摩根·弗里曼","id":"1054534"},{"alt":"https://movie.douban.com/celebrity/1041179/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p5837.webp","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p5837.webp","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p5837.webp"},"name":"鲍勃·冈顿","id":"1041179"}]
     * collect_count : 1263337
     * original_title : The Shawshank Redemption
     * subtype : movie
     * directors : [{"alt":"https://movie.douban.com/celebrity/1047973/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.webp","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.webp","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p230.webp"},"name":"弗兰克·德拉邦特","id":"1047973"}]
     * year : 1994
     * images : {"small":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp","large":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp","medium":"https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.webp"}
     * alt : https://movie.douban.com/subject/1292052/
     * id : 1292052
     */

    public RatingBean rating;
    public String title;
    public int collect_count;
    public String original_title;
    public String subtype;
    public String year;
    public Avatars images;
    public String alt;
    public String id;
    public List<String> genres;
    public List<Character> casts;
    public List<Character> directors;

    public static class RatingBean {
        /**
         * max : 10
         * average : 9.6
         * stars : 50
         * min : 0
         */

        public float max;
        public float average;
        public String stars = "";
        public float min;
    }


}
