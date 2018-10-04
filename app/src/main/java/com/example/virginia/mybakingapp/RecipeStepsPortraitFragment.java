package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.Unbinder;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeStepsPortraitFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_STEP_ID = "step_id";
    public static final String ARG_ITEMS = "items";
    private ArrayList<Recipe> recipes;
    private CollapsingToolbarLayout appBarLayout;
    private String stepId;
    private String itemId;
    MediaSessionCompat mMediaSession;
    private static final String TAG = RecipeStepsPortraitFragment.class.getSimpleName();
    private Context context;
    //Player related variables
    private PlaybackStateCompat.Builder mStateBuilder;
    private Button[] mButtons;
    private String CHANNEL_ID = "My_Video_channel_id";
    @BindView(R.id.playerView)
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private Unbinder unbinder;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepsPortraitFragment() {
    }

    RecipeViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ID)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            context = this.getContext();
            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

            itemId = getArguments().getString(ARG_ITEM_ID);
            stepId = getArguments().getString(ARG_STEP_ID);
            viewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Only get the recipe and set the Views if the system has data

        Recipe recipe = viewModel.getRecipes().getValue().get(Integer.parseInt(itemId) - 1);
        ArrayList<RecipeStep> recipeSteps = recipe.getSteps();
        //Add the name to the AppBar
        if (appBarLayout != null) {

            String addonText = getContext().getResources().getString(R.string.step);
            appBarLayout.setTitle(recipe.getName() + " " + addonText + " " + stepId);
        }
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (recipe != null) {
            TextView myLongDescription = ((TextView) rootView.findViewById(R.id.tv_step_description_intwopane));
            int stepIdint = Integer.parseInt(stepId) - 1;
            //TODO bring the proper description
            myLongDescription
                    .setText(recipeSteps.get(stepIdint).getDescription());
        }
        // Initialize the Media Session.
        mPlayerView = rootView.findViewById(R.id.playerView);
        getPlayer();
        mPlayerView.requestFocus();
        return rootView;
    }

    private void getPlayer() {
        // URL of the video to stream
        String videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4";

        TrackSelector trackSelector = new DefaultTrackSelector();

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "yourApplicationName"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoURL));

        LoadControl loadControl=new DefaultLoadControl();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context,trackSelector,loadControl);
        // Prepare the player with the source.
        exoPlayer.prepare(videoSource);

    }

}
