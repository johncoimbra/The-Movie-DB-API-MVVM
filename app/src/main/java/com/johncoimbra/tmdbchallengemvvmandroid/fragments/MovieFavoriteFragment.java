package com.johncoimbra.tmdbchallengemvvmandroid.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.johncoimbra.tmdbchallengemvvmandroid.R;
import com.johncoimbra.tmdbchallengemvvmandroid.adapters.MovieRecyclerView;
import com.johncoimbra.tmdbchallengemvvmandroid.adapters.favorites.FavoriteAdapter;
import com.johncoimbra.tmdbchallengemvvmandroid.adapters.OnMovieListener;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.utils.StarAnimationView;
import com.johncoimbra.tmdbchallengemvvmandroid.view.MovieDetailsActivity;
import com.johncoimbra.tmdbchallengemvvmandroid.viewmodel.MovieListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MovieFavoriteFragment extends Fragment implements OnMovieListener {

    // RecyclerView
    private RecyclerView recyclerListFavorite;
    private FavoriteAdapter favoriteAdapter;
    // ViewModel
    private MovieListViewModel movieListViewModel;
    private List<MovieModel> listFavorites = new ArrayList<>();
    //Animation
    private StarAnimationView starAnimationView;
    private FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        starAnimationView = view.findViewById(R.id.animated_view);
        frameLayout = view.findViewById(R.id.frame);
        recyclerListFavorite = view.findViewById(R.id.recyclerListMovies_now_playing);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        Paper.init(getActivity());
        readListFavorites();
        configureRecyclerView();

        starAnimationView.resume();

        return view;
    }


    // 5 - Iniciando o recyclerView e adicionando os dados
    private void configureRecyclerView() {
        // LiveData não precisa ser passado como parâmetro via construtor(lembrete)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favoriteAdapter = new FavoriteAdapter(listFavorites, this);
        recyclerListFavorite.setAdapter(favoriteAdapter);
        recyclerListFavorite.setLayoutManager(gridLayoutManager);
    }


    private void readListFavorites() {
        if (Paper.book().read("favorites") != null) {
            listFavorites = (Paper.book().read("favorites"));
        }

        if (listFavorites.size() == 0) {
            frameLayout.setVisibility(View.VISIBLE);
        } else {
            frameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra("movie", favoriteAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Paper.init(getActivity());
        readListFavorites();
        configureRecyclerView();
    }
}