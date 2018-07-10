package com.xxx.syy.ui.movie;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.syy.R;
import com.xxx.syy.entity.Character;
import com.xxx.syy.entity.Subjects;
import com.xxx.syy.ui.movie.detail.MovieDetailActivity;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-5-9.
 */

public class MovieAdapter extends BaseQuickAdapter<Subjects, BaseViewHolder> {

    private Activity mContext;

    MovieAdapter(Activity context, ArrayList<Subjects> datas) {
        super(R.layout.syy_item_movie, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Subjects item) {

        ((SimpleDraweeView) holder.getView(R.id.iv_syy_movie_item_avatar)).setImageURI(item.images.large);
        ((TextView) holder.getView(R.id.tv_syy_movie_item_name)).setText(item.title);

        StringBuilder sb = new StringBuilder(item.year);
        for (Character cast : item.casts) {
            if (sb.toString().length() > 40) {
                break;
            }
            sb.append("/").append(cast.name);
        }
        for (Character director : item.directors) {
            if (sb.toString().length() > 40) {
                break;
            }
            sb.append("/").append(director.name);
        }
        ((TextView) holder.getView(R.id.tv_syy_movie_item_detail)).setText(sb.toString());

        Subjects.RatingBean ratingBean = item.rating;
        float rate = ratingBean.average / ratingBean.max * 5;

        ((AppCompatRatingBar) holder.getView(R.id.rating_syy_movie_item_score)).setRating(rate);
        holder.getView(R.id.rating_syy_movie_item_score).setVisibility((int) rate == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.getView(R.id.tv_syy_movie_item_noscore).setVisibility((int) rate == 0 ? View.VISIBLE : View.GONE);
        ((TextView) holder.getView(R.id.tv_syy_movie_item_score)).setText(String.valueOf(ratingBean.average));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("url", item.images.large);
                intent.putExtra("title", item.title);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mContext);
                mContext.startActivity(intent, options.toBundle());
            }
        });
    }
}
