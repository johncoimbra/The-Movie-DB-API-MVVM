package com.johncoimbra.tmdbchallengemvvmandroid.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.johncoimbra.tmdbchallengemvvmandroid.executors.AppExecutors;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.response.MovieSearchResponse;
import com.johncoimbra.tmdbchallengemvvmandroid.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    // LiveData para search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;
    //Cria uma requisição Runnable global
    private RetrieveMoviesRunnable retrieveMoviesRunnable;
    // LiveData para popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPop;
    //Cria uma requisição Popular Runnable
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;
    // LiveData para now_playing
    private MutableLiveData<List<MovieModel>> mMoviesNowPlaying;
    //Cria uma requisição now_playing Runnable
    private RetrieveMoviesRunnableNowPlaying retrieveMoviesRunnableNowPlaying;


    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
        mMoviesNowPlaying = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }

    public LiveData<List<MovieModel>> getMoviesNowPlaying() {
        return mMoviesNowPlaying;
    }

    // 1 - Este será o método que serpa chamado por meio das classes
    public void searchMoviesApi(String query, int pageNumber) {
        if(retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().netWorkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().netWorkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancela a chamada para o Retrofit
                myHandler.cancel(true);

            }
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviesPop(int pageNumber) {
        if(retrieveMoviesRunnablePop != null){
            retrieveMoviesRunnablePop = null;
        }

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandlerPop = AppExecutors.getInstance().netWorkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().netWorkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancela a chamada para o Retrofit
                myHandlerPop.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviesNowPlaying(int pageNumber) {

        if(retrieveMoviesRunnableNowPlaying != null){
            retrieveMoviesRunnableNowPlaying = null;
        }

        retrieveMoviesRunnableNowPlaying = new RetrieveMoviesRunnableNowPlaying(pageNumber);

        final Future myHandlerNowPlaying = AppExecutors.getInstance().netWorkIO().submit(retrieveMoviesRunnableNowPlaying);

        AppExecutors.getInstance().netWorkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancela a chamada para o Retrofit
                myHandlerNowPlaying.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    //Recupera os dados da RESTApi pela class Runnable
    //Temos dois tipos de Queries: O ID e o search
    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Obtem a resposta
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        // Enviando dados para o LiveData
                        // postValue: usado para background thread
                        // setValue: usado para background thread
                        mMovies.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error: " + error);
                    mMovies.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMovies.postValue(null);
            }
        }

        // Search Method/ query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelando o pedido de pesquisa");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePop implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Obtem a resposta
            try {
                Response responsePop = getPop(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (responsePop.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) responsePop.body()).getMovies());
                    if (pageNumber == 1) {
                        // Enviando dados para o LiveData
                        // postValue: usado para background thread
                        // setValue: usado para background thread
                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                } else {
                    String error = responsePop.errorBody().string();
                    Log.v("Tag", "Error: " + error);
                    mMoviesPop.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getPop(int pageNumber) {
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelando o pedido de pesquisa");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnableNowPlaying implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnableNowPlaying(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Obtem a resposta
            try {
                Response responseNowPlaying = getNowPlaying(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (responseNowPlaying.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) responseNowPlaying.body()).getMovies());
                    if (pageNumber == 1) {
                        // Enviando dados para o LiveData
                        // postValue: usado para background thread
                        // setValue: usado para background thread
                        mMoviesNowPlaying.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesNowPlaying.getValue();
                        currentMovies.addAll(list);
                        mMoviesNowPlaying.postValue(currentMovies);
                    }
                } else {
                    String error = responseNowPlaying.errorBody().string();
                    Log.v("Tag", "Error: " + error);
                    mMoviesNowPlaying.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMoviesNowPlaying.postValue(null);
            }
        }

        private Call<MovieSearchResponse> getNowPlaying(int pageNumber) {
            return Servicey.getMovieApi().getNowPlaying(
                    Credentials.API_KEY,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelando o pedido de pesquisa");
            cancelRequest = true;
        }
    }
}