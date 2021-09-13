package com.johncoimbra.tmdbchallengemvvmandroid.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.request.MovieApiClient;
import java.util.List;

// Esta class atua como repositórios
public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient movieApiClient;
    private String mQuery;
    private int mPageNumber;

    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return movieApiClient.getMoviesPop();
    }
    public LiveData<List<MovieModel>> getNowPlaying() {
        return movieApiClient.getMoviesNowPlaying();
    }
    // 2 - Chamando o método searchMovie no repository
    public void searchMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesPop(pageNumber);
    }

    public void searchMovieNowPlaying(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesNowPlaying(pageNumber);
    }

    public void searchNextPage(){
        searchMovieApi(mQuery, mPageNumber + 1);
    }

    public void addItemListNowPlaying(int pageNumber) {
        mPageNumber = mPageNumber + pageNumber;
        movieApiClient.searchMoviesNowPlaying(mPageNumber);
        Log.w("pageNumber","pageNumber:" + mPageNumber);
    }

    public void addItemListPop(int pageNumber) {
        mPageNumber = mPageNumber + pageNumber;
        movieApiClient.searchMoviesPop(mPageNumber);
    }
}


