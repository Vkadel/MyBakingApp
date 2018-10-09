package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeStepsListFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_PLAYER_POSITION = "player_position";
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_STEP_ID = "step_id";
    public static final String ARG_ITEMS = "items";
    public static final String NEW_H = "new_h";
    public static final String NEW_W = "new_w";
    private ArrayList<Recipe> recipes;
    private Recipe recipe;
    private ArrayList<RecipeStep> steps;
    private CollapsingToolbarLayout appBarLayout;
    private String stepId;
    private String itemId;
    private static final String TAG = RecipeStepsPortraitFragment.class.getSimpleName();
    private Context context;
    @BindView(R.id.tv_step_description_intwopane)
    TextView myLongDescription;


    /*private PlayerView mPlayerView;*/
    private SimpleExoPlayer player;
    private String videoURL;
    //Player related variablesprivate PlaybackStateCompat.Builder mStateBuilder
    @BindView(R.id.playerView)
    PlayerView mPlayerView;
    Long videoWasPlayingat;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepsListFragment() {
    }

    RecipeViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ID)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            context = this.getContext();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            itemId = getArguments().getString(ARG_ITEM_ID);
            stepId = getArguments().getString(ARG_STEP_ID);
            viewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        }

    }

    private void releasePlayer() {
        player.release();
        player = null;
        mPlayerView.getOverlayFrameLayout().removeAllViews();
        videoURL = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (player != null) {
            mPlayerView.requestFocus();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null && videoWasPlayingat != null) {
            player.seekTo(videoWasPlayingat);
            player.setPlayWhenReady(true);
        } else {
            player.seekTo(0);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            videoWasPlayingat = savedInstanceState.getLong(ARG_PLAYER_POSITION);
        }
        //Only get the recipe and set the Views if the system has data
        recipe = viewModel.getRecipes().getValue().get(Integer.parseInt(itemId) - 1);
        steps = recipe.getSteps();
        ArrayList<RecipeStep> recipeStepserecipeSteps = recipe.getSteps();
        //Add the name to the AppBar
        if (appBarLayout != null) {
            appBarLayout.setTitle(recipe.getName());
        }
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this, rootView);
        // Show the dummy content as text in a TextView.
        if (recipe != null) {
            int stepIdint = Integer.parseInt(stepId) - 1;
            myLongDescription
                    .setText(recipeStepserecipeSteps.get(stepIdint).getDescription());
            initPlayer();
        }

        return rootView;
    }

    //setting up the player

    private void initPlayer() {
        // URL of the video to stream
        videoURL = steps.get(Integer.parseInt(stepId) - 1).getVideoURL();
        player = ExoPlayerFactory.newSimpleInstance(context);
        if (recipe != null && videoURL != null && !videoURL.isEmpty()) {
            got_video();
            mPlayerView.setPlayer(player);
            player.setPlayWhenReady(true);
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, getActivity().getApplication().getPackageName()));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoURL));

            // Prepare the player with the source.
            SetDescriptionAdjustPlayer();
            player.prepare(videoSource);
        } else {
            no_video();
        }
    }

    public void SetDescriptionAdjustPlayer() {
        //Set Description
        if (recipe != null && mPlayerView != null) {
            myLongDescription.setVisibility(View.VISIBLE);
            mPlayerView.requestFocus();
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                    mPlayerView.getLayoutParams();
            Bundle bundle = getScreenDims();
            if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                params.width = (int) (bundle.getInt(NEW_W) - getResources().getDimension(R.dimen.item_width));
                double doub = bundle.getInt(NEW_H) / 1.7;
                params.height = (int) doub;
            } else {
                params.width = (int) (bundle.getInt(NEW_W) - getResources().getDimension(R.dimen.item_width));
                double doub = bundle.getInt(NEW_W) / 1.8;
                params.height = (int) doub;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ARG_PLAYER_POSITION, player.getCurrentPosition());
        player.stop();
    }

    public Bundle getScreenDims() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Bundle bundle = new Bundle();
        bundle.putInt(NEW_H, height);
        bundle.putInt(NEW_W, width);
        return bundle;
    }

    public void no_video() {
        mPlayerView.setVisibility(View.GONE);
    }

    public void got_video() {
        mPlayerView.setVisibility(View.VISIBLE);
    }

}
