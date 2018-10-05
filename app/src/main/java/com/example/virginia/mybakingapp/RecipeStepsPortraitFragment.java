package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

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
    public static final String ARG_IS_PORTRAIT = "is_portrait";
    private Recipe recipe;
    private ArrayList<RecipeStep> steps;
    private CollapsingToolbarLayout appBarLayout;
    private String stepId;
    private String itemId;
    public Boolean isPortrait;
    RecipeViewModel viewModel;
    private static final String TAG = RecipeStepsPortraitFragment.class.getSimpleName();
    private Context context;
    @BindView(R.id.tv_step_description_intwopane) TextView myLongDescription;
    //Player related variablesprivate PlaybackStateCompat.Builder mStateBuilder
    @BindView(R.id.playerView)
    PlayerView mPlayerView;
    private String CHANNEL_ID = "My_Video_channel_id";
    /*private PlayerView mPlayerView;*/
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private MediaSource videoSource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepsPortraitFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlayerView.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        player.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        player.seekTo(0);
        player.setPlayWhenReady(true);
    }

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
            isPortrait=getArguments().getBoolean(ARG_IS_PORTRAIT);
            viewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPlayer();
        player.getBufferedPercentage();
        SetDescriptionAdjustPlayer();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation=this.getActivity().getRequestedOrientation();
        Timber.e("CONFIGURATION CHANGED"+orientation);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Only get the recipe and set the Views if the system has data

        recipe = viewModel.getRecipes().getValue().get(Integer.parseInt(itemId) - 1);
        steps = recipe.getSteps();
        if(savedInstanceState!=null){
            isPortrait=getActivity().getResources().getConfiguration().orientation==
                    getActivity().getResources().getConfiguration().ORIENTATION_PORTRAIT;}
        //Inflate Rootview
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this,rootView);
        //Add the name to the AppBar
        if (appBarLayout != null) {
            String addonText = getContext().getResources().getString(R.string.step);
            appBarLayout.setTitle(recipe.getName() + " " + addonText + " " + stepId);
        }

        SetDescriptionAdjustPlayer();
        return rootView;

    }

public void setUpisPortraitRecipeStepFragment(boolean isport){
        isPortrait=isport;
}
    private void initPlayer() {
        // URL of the video to stream
        String videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4";
        player = ExoPlayerFactory.newSimpleInstance(context);
        mPlayerView.setPlayer(player);
        player.setPlayWhenReady(true);
        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, getActivity().getApplication().getPackageName()));
        // This is the MediaSource representing the media to be played.
        videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoURL));

        // Prepare the player with the source.
        player.prepare(videoSource);
    }
    public void SetDescriptionAdjustPlayer(){
        //Set Description
        if (recipe != null) {
            if(isPortrait){
                myLongDescription.setVisibility(View.VISIBLE);
                int stepIdint = Integer.parseInt(stepId) - 1;
                //Get proper description for this particular step
                myLongDescription.setText(steps.get(stepIdint).getDescription());
                ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams)
                        mPlayerView.getLayoutParams();
                params.width=Math.round(getResources().getDimension(R.dimen.player_view_port_width));
                params.height=Math.round(getResources().getDimension(R.dimen.player_view_port_width))/2;}
                else {
                myLongDescription.setVisibility(View.INVISIBLE);
                mPlayerView.requestFocus();
                ConstraintLayout.LayoutParams params=(ConstraintLayout.LayoutParams)
                        mPlayerView.getLayoutParams();
                params.width=Math.round(getResources().getDimension(R.dimen.player_view_land_width));
                params.height=(int)Math.round(Math.round(getResources().getDimension(R.dimen.player_view_land_width))/2.1);;

            }
        }
    }

}
