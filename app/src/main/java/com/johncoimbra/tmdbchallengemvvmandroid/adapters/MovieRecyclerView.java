package com.johncoimbra.tmdbchallengemvvmandroid.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.johncoimbra.tmdbchallengemvvmandroid.R;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;
    private static final int DISPLAY_POPULAR = 1;
    private static final int DISPLAY_SEARCH = 1;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == DISPLAY_SEARCH){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_layout, parent, false);
            return new MovieViewHolder(view, onMovieListener);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_layout, parent, false);
            return new PopularViewHolder(view, onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder)holder).title.setText(mMovies.get(position).getTitle());
        int itemViewType = getItemViewType(position);
        if(itemViewType == DISPLAY_SEARCH){
            Glide.with(holder.itemView.getContext())
                    .load(Credentials.BASE_URL_IMAGE + mMovies.get(position).getPoster_path())
                    .into(((MovieViewHolder) holder).imageView);
        }else {
            Glide.with(holder.itemView.getContext())
                    .load(Credentials.BASE_URL_IMAGE + mMovies.get(position).getPoster_path())
                    .into(((PopularViewHolder) holder).imageViePop);
        }
    }

    @Override
    public int getItemCount() {
        if(mMovies != null){
            return mMovies.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    // Obtém o ID do filme selecionado
    public MovieModel getSelectedMovie(int position){
        if(mMovies != null){
            if(mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR){
            return DISPLAY_POPULAR;
        }
        else
            return DISPLAY_SEARCH;
    }

    //------------------------------ Movie - ViwHolder-----------------------------------//
    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Elementos/Objetos do layout
        TextView title;
        ImageView imageView;
        //Interface Click Listener "OnMovieListener"
        OnMovieListener onMovieListener;

        public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            this.onMovieListener = onMovieListener;

            title = itemView.findViewById(R.id.movie_title);
            imageView = itemView.findViewById(R.id.movie_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }

    //------------------------------ Movie Popular - ViwHolder-----------------------------------//

    public static class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageViePop;
        OnMovieListener listener;
        OnMovieListener onMovieListener;

        public PopularViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            this.listener = onMovieListener;

            imageViePop = itemView.findViewById(R.id.movie_img_popualar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }
}
