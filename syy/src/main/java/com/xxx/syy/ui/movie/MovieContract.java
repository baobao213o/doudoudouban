package com.xxx.syy.ui.movie;

import com.xxx.library.mvp.view.IView;
import com.xxx.syy.entity.Top250MovieInfo;
import com.xxx.syy.entity.USBoxMovieInfo;

/**
 * Created by gaoruochen on 18-4-16.
 */

public interface MovieContract {

    interface View extends IView{

        void showTop250Movies(Top250MovieInfo movieInfo);

        void showUSboxMovies(USBoxMovieInfo movieInfo);
    }

}
