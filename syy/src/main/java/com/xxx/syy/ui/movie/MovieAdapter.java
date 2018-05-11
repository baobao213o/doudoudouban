package com.xxx.syy.ui.movie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.library.glide.GlideApp;
import com.xxx.syy.R;
import com.xxx.syy.entity.Subjects;

import java.util.ArrayList;

/**
 * Created by gaoruochen on 18-5-9.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Context mContext;
    private ArrayList<Subjects> mDatas;

    MovieAdapter(Context context, ArrayList<Subjects> datas) {
        super();
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setList(ArrayList<Subjects> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.syy_item_movie, null);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Subjects subjects = mDatas.get(position);
        GlideApp.with(mContext).load(subjects.images.large).into(holder.iv_syy_movie_item_avatar);
        holder.tv_syy_movie_item_name.setText(subjects.title);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        private ImageView iv_syy_movie_item_avatar;
        private TextView tv_syy_movie_item_name;

        MovieHolder(View view) {
            super(view);
            iv_syy_movie_item_avatar = view.findViewById(R.id.iv_syy_movie_item_avatar);
            tv_syy_movie_item_name = view.findViewById(R.id.tv_syy_movie_item_name);
        }
    }


}
