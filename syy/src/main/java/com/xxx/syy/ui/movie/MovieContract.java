package com.xxx.syy.ui.movie;

import com.xxx.library.mvp.view.IView;
import com.xxx.syy.entity.MovieInfo;

/**
 * Created by gaoruochen on 18-4-16.
 */

interface MovieContract {

    interface View extends IView{

        void showTop250Movies(MovieInfo movieInfo);


    }

}
