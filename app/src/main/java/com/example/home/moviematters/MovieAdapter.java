package com.example.home.moviematters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 2/2/2016
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {
    private LayoutInflater inflater;
    List<Result> data = Collections.emptyList();
    private Context mContext;
    private Result mResult;


    public MovieAdapter(Context context,List<Result> data){
        inflater=LayoutInflater.from(context);
        this.data = data;
        this.mContext = context;
    }

    public void updateData(List<Result> newData){
        data = newData;
        notifyDataSetChanged();
    }
    public void clearList(){
        int size = this.data.size();
        if(size > 0){
            for (int i = 0;i<size;i++) {
                data.remove(i);
            }
            this.notifyItemRangeRemoved(0,size);
        }
    }
    public Result getItem(int position){
        return data.get(position);
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_movie, parent, false);
        PosterViewHolder viewHolder = new PosterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        Result current = data.get(position);
        String poster_path = Uri.parse("http://image.tmdb.org/t/p/w185").buildUpon()
                .appendEncodedPath(current.getPosterPath())
                .build().toString();

        Picasso.with(mContext).load(poster_path).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        if (data == null){
            return -1;
        }
        return data.size();
    }


    class PosterViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView = (ImageView)itemView.findViewById(R.id.list_item_poster);



        }
    }

}

