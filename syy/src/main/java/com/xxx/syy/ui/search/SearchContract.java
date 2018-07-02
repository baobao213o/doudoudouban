package com.xxx.syy.ui.search;

import com.xxx.library.mvp.view.IView;
import com.xxx.syy.entity.History;
import com.xxx.syy.entity.SearchMovieInfo;

import java.util.List;

/**
 * Created by gaoruochen on 18-4-16.
 */

public interface SearchContract {

    interface View extends IView{

        void showSearchMovies(SearchMovieInfo movieInfo);

        void showHistory(List<History> histories);

    }

}
