package com.johncoimbra.tmdbchallengemvvmandroid.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import android.widget.Toast;

import com.johncoimbra.tmdbchallengemvvmandroid.R;
import com.johncoimbra.tmdbchallengemvvmandroid.adapters.MovieRecyclerView;
import com.johncoimbra.tmdbchallengemvvmandroid.adapters.OnMovieListener;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.view.MovieDetailsActivity;
import com.johncoimbra.tmdbchallengemvvmandroid.viewmodel.MovieListViewModel;

import java.util.List;

public class MovieTopFragment extends Fragment implements OnMovieListener {

    // RecyclerView
    private RecyclerView recyclerListNowPlaying;
    private MovieRecyclerView movieRecyclerAdapter;
    // ViewModel
    private MovieListViewModel movieListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_top, container, false);

        recyclerListNowPlaying = view.findViewById(R.id.recyclerListMovies_now_playing);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        // Chamando o método do recyclerView
        configureRecyclerView();
        // Chamando os Observers
        observeNowPlaying();
        // Obtem os filmes mais assistidos do momento (now_playing)
        movieListViewModel.searchMovieNowPlaying(1);

        return view;
    }

    private void observeNowPlaying() {
        movieListViewModel.getMovieNowPlaying().observe(getActivity(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observando qualquer mudança de dados
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        // Pega os dados no log
                        Log.v("Tag", "onChanged: " + movieModel.getTitle());
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }
            }
        });
    }

    // 5 - Iniciando o recyclerView e adicionando os dados
    private void configureRecyclerView(){
        // LiveData não precisa ser passado como parâmetro via construtor(lembrete)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        movieRecyclerAdapter = new MovieRecyclerView(this);
        recyclerListNowPlaying.setAdapter(movieRecyclerAdapter);
        recyclerListNowPlaying.setLayoutManager(gridLayoutManager);

        // Paginação no RecyclerView
        // Carrega a próxima página da API Response
        recyclerListNowPlaying.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    // Exibe os próximos resultados da pesquisa na próxima página da API
                    // movieListViewModel.searchNextPage();
                    movieListViewModel.addItemsList(1);
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);
    }
}