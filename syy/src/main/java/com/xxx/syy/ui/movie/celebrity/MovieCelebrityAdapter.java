package com.xxx.syy.ui.movie.celebrity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.syy.R;
import com.xxx.syy.entity.Character;
import com.xxx.syy.entity.Subjects;
import com.xxx.syy.ui.movie.detail.MovieDetailActivity;

import java.util.ArrayList;

public class MovieCelebrityAdapter extends BaseQuickAdapter<Subjects, MovieCelebrityAdapter.MovieHolder> {

    private Activity mContext;

    MovieCelebrityAdapter(Activity context, ArrayList<Subjects> datas) {
        super(R.layout.syy_item_celebrity_movie, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(MovieCelebrityAdapter.MovieHolder holder, final Subjects item) {

        holder.iv_syy_celebrity_movie_item_avatar.setImageURI(item.images.large);
        holder.tv_syy_celebrity_movie_item_name.setText(item.title);
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
        holder.tv_syy_celebrity_movie_item_detail.setText(sb.toString());

        Subjects.RatingBean ratingBean = item.rating;
        float rate = ratingBean.average / ratingBean.max * 5;

        holder.rating_syy_celebrity_movie_item_score.setRating(rate);
        holder.rating_syy_celebrity_movie_item_score.setVisibility((int) rate == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.tv_syy_celebrity_movie_item_noscore.setVisibility((int) rate == 0 ? View.VISIBLE : View.GONE);

        holder.tv_syy_celebrity_movie_item_score.setText(String.valueOf(ratingBean.average));

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

    public static class MovieHolder extends BaseViewHolder {

        private SimpleDraweeView iv_syy_celebrity_movie_item_avatar;
        private TextView tv_syy_celebrity_movie_item_name, tv_syy_celebrity_movie_item_detail, tv_syy_celebrity_movie_item_score, tv_syy_celebrity_movie_item_noscore;
        private RatingBar rating_syy_celebrity_movie_item_score;

        public MovieHolder(View view) {
            super(view);
            iv_syy_celebrity_movie_item_avatar = view.findViewById(R.id.iv_syy_celebrity_movie_item_avatar);
            tv_syy_celebrity_movie_item_name = view.findViewById(R.id.tv_syy_celebrity_movie_item_name);
            rating_syy_celebrity_movie_item_score = view.findViewById(R.id.rating_syy_celebrity_movie_item_score);
            tv_syy_celebrity_movie_item_detail = view.findViewById(R.id.tv_syy_celebrity_movie_item_detail);
            tv_syy_celebrity_movie_item_score = view.findViewById(R.id.tv_syy_celebrity_movie_item_score);
            tv_syy_celebrity_movie_item_noscore = view.findViewById(R.id.tv_syy_celebrity_movie_item_noscore);
        }
    }

}
