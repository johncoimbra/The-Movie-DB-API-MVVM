package com.johncoimbra.tmdbchallengemvvmandroid.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Objects;

public class MoviePopularFragment extends Fragment implements OnMovieListener {

    //RecyclerView
    private RecyclerView recyclerListMovies;
    private MovieRecyclerView movieRecyclerAdapter;
    //SearchView
    private SearchView searchView = null;
    // ViewModel
    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_popular, container, false);

        searchView = view.findViewById(R.id.search_view);

        // Chamando o método de pesquisa de filmes da Api
        setupSearchView();

        recyclerListMovies = view.findViewById(R.id.recyclerListMovies);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        // Chamando o método do recyclerView
        configureRecyclerView();
        // Chamando os Observers
        observeAnyChange();
        observePopularMovies();

        //Obtem os filmes populares
        movieListViewModel.searchMoviePop(1);

        return view;
    }

    private void observePopularMovies() {
        movieListViewModel.getMoviesPop().observe(getActivity(), new Observer<List<MovieModel>>() {
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

    // Observando qualquer mudança de dados
    private void observeAnyChange(){
        movieListViewModel.getMovies().observe(getActivity(), new Observer<List<MovieModel>>() {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        movieRecyclerAdapter = new MovieRecyclerView(this);
        recyclerListMovies.setAdapter(movieRecyclerAdapter);
        recyclerListMovies.setLayoutManager(gridLayoutManager);

        // Paginação no RecyclerView
        // Carrega a próxima página da API Response
        recyclerListMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    // Exibe os próximos resultados da pesquisa na próxima página da API
                    movieListViewModel.searchNextPage();
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

    // Obtém os dados pela searchview & consulta a API para obter resultados (filmes)
    private void setupSearchView() {
        if(searchView != null){
            searchView = searchView.findViewById(R.id.search_view);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Texto(String) obtido pelo SearchView
                    movieListViewModel.searchMovieApi(query, 1);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isPopular = false;
                }
            });
        }else{
            try {
                Toast.makeText(getContext(), "O App parou de funcionar!", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}