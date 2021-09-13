package com.johncoimbra.tmdbchallengemvvmandroid.adapters.favorites;

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
import com.johncoimbra.tmdbchallengemvvmandroid.adapters.OnMovieListener;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.utils.Credentials;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> listFavorites;
    private OnMovieListener onMovieListener;

    @SuppressLint("NotifyDataSetChanged")
    public FavoriteAdapter(List<MovieModel> listFavorites, OnMovieListener onMovieListener) {
            this.listFavorites = listFavorites;
            this.onMovieListener = onMovieListener;
            notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_layout, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view, onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FavoriteAdapter.FavoriteViewHolder)holder).title.setText(listFavorites.get(position).getTitle());

        Glide.with(holder.itemView.getContext())
                .load(Credentials.BASE_URL_IMAGE + listFavorites.get(position).getPoster_path())
                .into(((FavoriteAdapter.FavoriteViewHolder) holder).imageView);
    }

    @Override
    public int getItemCount() {
        if(listFavorites != null){
            return listFavorites.size();
        }
        return 0;
    }

    // Obtém o ID do filme selecionado
    public MovieModel getSelectedMovie(int position){
        if(listFavorites != null){
            if(listFavorites.size() > 0){
                return listFavorites.get(position);
            }
        }
        return null;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView imageView;
        OnMovieListener onMovieListener;

        public FavoriteViewHolder(@NonNull View itemView, OnMovieListener listener) {
            super(itemView);
            this.onMovieListener = listener;

            title = itemView.findViewById(R.id.movie_title);
            imageView = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }
}
