package com.xxx.syy.ui.movie;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xxx.library.glide.GlideApp;
import com.xxx.syy.R;
import com.xxx.syy.entity.Subjects;
import com.xxx.syy.ui.movie.detail.MovieDetailActivity;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-5-9.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Activity mContext;
    private ArrayList<Subjects> mDatas;

    MovieAdapter(Activity context, ArrayList<Subjects> datas) {
        super();
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setList(ArrayList<Subjects> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public ArrayList<Subjects> getList() {
        return mDatas;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.syy_item_movie, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, int position) {
        final Subjects subjects = mDatas.get(position);
        GlideApp.with(mContext).load(subjects.images.large).into(holder.iv_syy_movie_item_avatar);
        holder.tv_syy_movie_item_name.setText(subjects.title);
        StringBuilder sb = new StringBuilder(subjects.year);
        for (Subjects.Character cast : subjects.casts) {
            sb.append("/").append(cast.name);
        }
        for (Subjects.Character director : subjects.directors) {
            sb.append("/").append(director.name);
        }
        holder.tv_syy_movie_item_detail.setText(sb.toString());

        Subjects.RatingBean ratingBean = subjects.rating;
        float rate = ratingBean.average / ratingBean.max * 5;

        holder.rating_syy_movie_item_score.setRating(rate);
        holder.rating_syy_movie_item_score.setVisibility((int)rate == 0 ? View.INVISIBLE : View.VISIBLE);
        holder.tv_syy_movie_item_noscore.setVisibility((int)rate == 0 ? View.VISIBLE : View.GONE);

        holder.tv_syy_movie_item_score.setText(ratingBean.average + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra("id", subjects.id);
                intent.putExtra("url", subjects.images.large);
                intent.putExtra("title", subjects.title);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        private ImageView iv_syy_movie_item_avatar;
        private TextView tv_syy_movie_item_name, tv_syy_movie_item_detail, tv_syy_movie_item_score, tv_syy_movie_item_noscore;
        private RatingBar rating_syy_movie_item_score;

        MovieHolder(View view) {
            super(view);
            iv_syy_movie_item_avatar = view.findViewById(R.id.iv_syy_movie_item_avatar);
            tv_syy_movie_item_name = view.findViewById(R.id.tv_syy_movie_item_name);
            rating_syy_movie_item_score = view.findViewById(R.id.rating_syy_movie_item_score);
            tv_syy_movie_item_detail = view.findViewById(R.id.tv_syy_movie_item_detail);
            tv_syy_movie_item_score = view.findViewById(R.id.tv_syy_movie_item_score);
            tv_syy_movie_item_noscore = view.findViewById(R.id.tv_syy_movie_item_noscore);
        }
    }


}
