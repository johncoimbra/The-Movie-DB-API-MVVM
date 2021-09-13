package com.johncoimbra.tmdbchallengemvvmandroid.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;

// Esta class faz uma solicitação(request) para um único filme(movie)
public class MovieResponse {
    // 1 - Encontrando o objeto filme
    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie(){
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
