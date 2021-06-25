package com.codepath.cchem.flixster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.cchem.flixster.models.Movie;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvGenre;
    RatingBar rbVoteAverage;
    TextView tvRatingNo;
    TextView tvOverview;
    ImageView ivPoster;

    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_movie_details);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvGenre = findViewById(R.id.tvGenre);
        ivPoster=findViewById(R.id.imgMovieRounded);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvRatingNo = findViewById(R.id.tvRatingNo);
        tvOverview = (TextView) findViewById(R.id.tvOverview);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getYoutubekey()));

        // Youtube
        youTubePlayerView = findViewById(R.id.activity_details_video_trailer_youtube);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = movie.getYoutubekey();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        // Enter real values from API
        tvTitle.setText(movie.getTitle());
        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);
        tvRatingNo.setText(voteAverage+"");
        tvOverview.setText(movie.getOverview());

        String imageUrl;
        // Image url changes based on portrait or landscape modes
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = movie.getBackdropPath();
        } else {
            imageUrl = movie.getPosterPath();
        }

        Glide.with(this)
                .load(imageUrl)
                .transform(new FitCenter(),new RoundedCorners(25)).apply(RequestOptions.circleCropTransform())
                .into(ivPoster);

    }
}