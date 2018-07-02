package com.xxx.syy.ui.movie.detail;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xxx.syy.R;
import com.xxx.syy.entity.Character;
import com.xxx.syy.ui.movie.celebrity.MovieCelebrityActivity;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-5-9.
 */

public class MovieCharatersAdapter extends BaseQuickAdapter<Character, MovieCharatersAdapter.MovieHolder> {

    private Activity activity;
    private int color;

    MovieCharatersAdapter(Activity context, ArrayList<Character> datas) {
        super(R.layout.syy_item_movie_cast, datas);
        this.activity = context;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void convert(MovieHolder holder, final Character character) {
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

    class MovieHolder extends BaseViewHolder {

        private SimpleDraweeView iv_syy_movie_cast_avatar;
        private TextView tv_syy_movie_cast_name;

        MovieHolder(View view) {
            super(view);
            iv_syy_movie_cast_avatar = view.findViewById(R.id.iv_syy_movie_cast_avatar);
            tv_syy_movie_cast_name = view.findViewById(R.id.tv_syy_movie_cast_name);
        }
    }


}
