package com.xxx.syy.ui.movie.detail;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.syy.R;
import com.xxx.syy.entity.Character;
import com.xxx.syy.ui.movie.celebrity.MovieCelebrityActivity;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-5-9.
 */

public class MovieCharatersAdapter extends RecyclerView.Adapter<MovieCharatersAdapter.MovieHolder> {

    private ArrayList<Character> mDatas;
    private Activity activity;
    private int color;

    MovieCharatersAdapter(Activity activity, ArrayList<Character> datas) {
        super();
        this.mDatas = datas;
        this.activity = activity;
    }

    public void setList(ArrayList<Character> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ArrayList<Character> getList() {
        return mDatas;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.syy_item_movie_cast, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieHolder holder, int position) {
        final Character character = mDatas.get(position);
        String url;
        try {
            url = character.avatars.large;
        } catch (Exception e) {
            url = "";
        }
        holder.iv_syy_movie_cast_avatar.setImageURI(url);
        holder.tv_syy_movie_cast_name.setText(character.name);
        final String finalUrl = url;
        holder.iv_syy_movie_cast_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MovieCelebrityActivity.class);
                intent.putExtra("id", character.id);
                intent.putExtra("url", finalUrl);
                intent.putExtra("color", color == 0 ? activity.getResources().getColor(R.color.colorPrimary) : color);
                intent.putExtra("name", character.name);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, Pair.create(v, activity.getString(R.string.syy_transition_movie_detail_img)));
                activity.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView iv_syy_movie_cast_avatar;
        private TextView tv_syy_movie_cast_name;

        MovieHolder(View view) {
            super(view);
            iv_syy_movie_cast_avatar = view.findViewById(R.id.iv_syy_movie_cast_avatar);
            tv_syy_movie_cast_name = view.findViewById(R.id.tv_syy_movie_cast_name);
        }
    }


}
