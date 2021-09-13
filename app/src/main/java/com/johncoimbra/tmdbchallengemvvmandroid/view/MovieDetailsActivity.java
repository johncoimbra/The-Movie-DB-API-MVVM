package com.johncoimbra.tmdbchallengemvvmandroid.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.johncoimbra.tmdbchallengemvvmandroid.R;
import com.johncoimbra.tmdbchallengemvvmandroid.models.MovieModel;
import com.johncoimbra.tmdbchallengemvvmandroid.utils.Credentials;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView imageViewDetails;
    private TextView titleDetails, descDetails;
    private ImageButton imgButton;
    private MovieModel movieModel;
    private boolean isFavorite = false;
    private List<MovieModel> listFavorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Paper.init(this);

        imageViewDetails = findViewById(R.id.imageView_details);
        titleDetails = findViewById(R.id.textView_title_details);
        descDetails = findViewById(R.id.textView_desc_details);
        imgButton = findViewById(R.id.fav_btn);

        getDataFromIntent();
        favoriteButton();
        readListFavorites();
    }

    // Evento para alterar cor do botao(coracao),
    // limpar ou adicionar dados na lista atual e gravar na lista do PAPER
    private void favoriteButton() {
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgButton.setImageResource(0); // Limpa imagem para outra assumir a posição
                isFavorite = !isFavorite;
                Log.v("isFavorite", "Resultado: " + isFavorite);

                if (!isFavorite) {
                    imgButton.setBackgroundResource(R.drawable.ic_fav_off);
                    for (int i = 0; i < listFavorites.size(); i++) {
                        if (listFavorites.get(i).getTitle().contains(movieModel.getTitle())) {
                            listFavorites.remove(i);
                            Paper.book().delete("favorites");
                            Paper.book().write("favorites", listFavorites);
                        }
                    }
                } else {
                    imgButton.setBackgroundResource(R.drawable.ic_fav_on);
                    listFavorites.add(movieModel);
                    Paper.book().write("favorites", listFavorites);
                }
                readListFavorites();
            }
        });
    }

    // Somente executado quando entrar na tela a primeira vez
    private void changeButtonFavorite() {
        imgButton.setImageResource(0); // Limpa imagem para outra assumir a posição
        if (isFavorite) {
            imgButton.setBackgroundResource(R.drawable.ic_fav_on);
        } else {
            imgButton.setBackgroundResource(R.drawable.ic_fav_off);
        }
    }

    private void readListFavorites() {
        if (Paper.book().read("favorites") != null) {
            listFavorites = (Paper.book().read("favorites"));

            Log.v("listFavorites", "Quantidade de items que já foram salvos: " + listFavorites.size());
            Log.v("Filmes", "filmes: " + listFavorites.size());
            Log.v("Filmes", "FilmeModel: " + movieModel.getTitle());

            for (int i = 0; i < listFavorites.size(); i++) {
                Log.v("Filmes", "Filmeslista: " + listFavorites.get(i).getTitle());
                if (listFavorites.get(i).getTitle().contains(movieModel.getTitle())) {
                    Log.v("Filmes", "caiu no IF: ");
                    isFavorite = true;
                    changeButtonFavorite();
                }
            }
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("movie")) {
            movieModel = getIntent().getParcelableExtra("movie");
            Log.v("MovieModel", "Resultado: " + movieModel);
            titleDetails.setText(movieModel.getTitle());
            descDetails.setText(movieModel.getMovie_overview());
            Glide.with(this)
                    .load(Credentials.BASE_URL_IMAGE + movieModel.getPoster_path())
                    .into(imageViewDetails);
        } else {
            Toast.makeText(this, "Os dados não foram encontrados", Toast.LENGTH_SHORT).show();
        }
    }
}