package com.johncoimbra.tmdbchallengemvvmandroid.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.repositories.MovieRepository;

import java.util.List;

// Esta class é usada para ViewModel
public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;
    // Construtor
    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getMoviesPop(){
        return movieRepository.getPop();
    }
    public LiveData<List<MovieModel>> getMovieNowPlaying(){
        return movieRepository.getNowPlaying();
    }

    // 3 - Chamando método na ViewModel
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query, pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        movieRepository.searchMoviePop(pageNumber);
    }

    public void searchMovieNowPlaying(int pageNumber){
        movieRepository.searchMovieNowPlaying(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }

    public  void addItemsListPop(int pageNumber) {
        movieRepository.addItemListPop(pageNumber);
    }

    public  void addItemsList(int pageNumber) {
        movieRepository.addItemListNowPlaying(pageNumber);
    }
}
