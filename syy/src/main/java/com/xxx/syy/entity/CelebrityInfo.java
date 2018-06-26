package com.xxx.syy.entity;

import java.util.List;


public class CelebrityInfo {


    /**
     * mobile_url : https://movie.douban.com/celebrity/1054395/mobile
     * aka_en : ["Elijah Jordan Wood (本名)","Elwood, Lij and Monkey (昵称)"]
     * name : 伊莱贾·伍德
     * works : [{"roles":["演员"],"subject":{"rating":{"max":10,"average":9.1,"stars":"45","min":0},"genres":["喜剧","科幻"],"title":"全能侦探社 第二季","casts":[{"alt":"https://movie.douban.com/celebrity/1045095/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p2491.webp","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p2491.webp","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p2491.webp"},"name":"塞缪尔·巴奈特","id":"1045095"}],"collect_count":4150,"original_title":"Dirk Gently's Holistic Detective Agency","subtype":"tv","directors":[{"alt":"https://movie.douban.com/celebrity/1293954/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1374043154.58.webp","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1374043154.58.webp","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1374043154.58.webp"},"name":"迪恩·帕里索特","id":"1293954"}],"year":"2017","images":{"small":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2502201879.webp","large":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2502201879.webp","medium":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2502201879.webp"},"alt":"https://movie.douban.com/subject/26946433/","id":"26946433"}}]
     * gender : 男
     * avatars : {"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p51597.webp","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p51597.webp","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p51597.webp"}
     * id : 1054395
     * aka : ["伊莱贾·伍德"]
     * name_en : Elijah Wood
     * born_place : 美国,爱荷华州,锡达拉皮兹
     * alt : https://movie.douban.com/celebrity/1054395/
     */

    public String mobile_url;
    public String name;
    public String gender;
    public Avatars avatars;
    public String id;
    public String name_en;
    public String born_place;
    public String alt;
    public List<String> aka_en;
    public List<WorksBean> works;
    public List<String> aka;


    public static class WorksBean {
        /**
         * roles : ["演员"]
         * subject : {"rating":{"max":10,"average":9.1,"stars":"45","min":0},"genres":["喜剧","科幻"],"title":"全能侦探社 第二季","casts":[{"alt":"https://movie.douban.com/celebrity/1045095/","avatars":{"small":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p2491.webp","large":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p2491.webp","medium":"https://img3.doubanio.com/view/celebrity/s_ratio_celebrity/public/p2491.webp"},"name":"塞缪尔·巴奈特","id":"1045095"}],"collect_count":4150,"original_title":"Dirk Gently's Holistic Detective Agency","subtype":"tv","directors":[{"alt":"https://movie.douban.com/celebrity/1293954/","avatars":{"small":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1374043154.58.webp","large":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1374043154.58.webp","medium":"https://img1.doubanio.com/view/celebrity/s_ratio_celebrity/public/p1374043154.58.webp"},"name":"迪恩·帕里索特","id":"1293954"}],"year":"2017","images":{"small":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2502201879.webp","large":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2502201879.webp","medium":"https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2502201879.webp"},"alt":"https://movie.douban.com/subject/26946433/","id":"26946433"}
         */

        public Subjects subject;
        public List<String> roles;

    }
}
