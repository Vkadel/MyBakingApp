package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final String ARG_IS_PORTRAIT = "is_portrait";
    public static final String ARG_IS_LANDSCAPE_AND_SMALL = "is_landscape_and_small";
    private static final String SCREN_H="screen_h";
    private static final String SCREEN_W="screen_w";
    private Recipe recipe;
    private ArrayList<RecipeStep> steps;
    private CollapsingToolbarLayout appBarLayout;
    private String stepId;
    private String itemId;
    public Boolean isPortrait;
    RecipeViewModel viewModel;
    private Fragment fragmentSteps;
    private static final String TAG = RecipeStepsPortraitFragment.class.getSimpleName();
    private Context context;
    @Nullable
    @BindView(R.id.previous_step_but)
    Button prevStep;
    @Nullable
    @BindView(R.id.next_step_but)
    Button nextStep;
    @BindView(R.id.tv_step_description_intwopane)
    TextView myLongDescription;

    Toast toast;

    //Player related variables
    private SimpleExoPlayer player;
    @BindView(R.id.playerView)
    PlayerView mPlayerView;
    private String videoURL;

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
            if (player != null) {
                player.release();
                player = null;
                mPlayerView.getOverlayFrameLayout().removeAllViews();
                videoURL = null;
            }
        }
    }

    @Override
    public void onStart() {
        if (player != null) {
            super.onStart();
            mPlayerView.requestFocus();
        }
    }

    @Override
    public void onPause() {
        if (player != null) {
            super.onPause();
            player.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.seekTo(0);
            player.setPlayWhenReady(true);
        }
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
            appBarLayout = activity.findViewById(R.id.toolbar_layout);
            itemId = getArguments().getString(ARG_ITEM_ID);
            stepId = getArguments().getString(ARG_STEP_ID);
            isPortrait = getArguments().getBoolean(ARG_IS_PORTRAIT);
            viewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
            recipe = viewModel.getRecipes().getValue().get(Integer.parseInt(itemId) - 1);
            steps = recipe.getSteps();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPlayer();
        SetDescriptionAdjustPlayer();
        int screenSize=Integer.parseInt(getScreenSize().getString(SCREEN_W));
        //On Click listener for buttom that loads the previous step
        if (isPortrait && nextStep!=null){
        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(stepId) ==1){
                    Toast.makeText(context, context.getResources()
                            .getString(R.string.first_step), Toast.LENGTH_SHORT).show();
                }
                if (Integer.parseInt(stepId) >1) {
                    String nextStep = String.valueOf(Integer.parseInt(stepId) - 1);
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepsListFragment.ARG_ITEM_ID, itemId);
                    intent.putExtra(RecipeStepsListFragment.ARG_STEP_ID, nextStep);
                    context.startActivity(intent);
                }
            }
        });
        //On Click listener for buttom that loads the next step
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clicking the next StepDetail Activity
                if(Integer.parseInt(stepId) == recipe.getSteps().size()){
                    Toast.makeText(context, context.getResources()
                            .getString(R.string.last_step), Toast.LENGTH_SHORT).show();
                }
                if (Integer.parseInt(stepId) < recipe.getSteps().size()) {
                    String nextStep = String.valueOf(Integer.parseInt(stepId) + 1);
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepsListFragment.ARG_ITEM_ID, itemId);
                    intent.putExtra(RecipeStepsListFragment.ARG_STEP_ID, nextStep);
                    context.startActivity(intent);
                }
            }
        });
    }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = this.getActivity().getRequestedOrientation();
        Timber.e("CONFIGURATION CHANGED" + orientation);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Only get the recipe and set the Views if the system has data


        if (savedInstanceState != null) {
            isPortrait = getActivity().getResources().getConfiguration().orientation ==
                    getActivity().getResources().getConfiguration().ORIENTATION_PORTRAIT;
        }
        //Inflate Rootview
        View rootView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this, rootView);
        //Add the name to the AppBar
        if (appBarLayout != null) {
            String addonText = getContext().getResources().getString(R.string.step);
            appBarLayout.setTitle(recipe.getName() + " " + addonText + " " + stepId);
        }
        return rootView;
    }

    public void setUpisPortraitRecipeStepFragment(boolean isport) {
        isPortrait = isport;
    }

    public void setUpStepId(String StepId){stepId=StepId;}

    private void initPlayer() {
        // URL of the video to stream
        videoURL = steps.get(Integer.parseInt(stepId) - 1).getVideoURL();
        player = ExoPlayerFactory.newSimpleInstance(context);
        if (recipe != null && videoURL != null && !videoURL.isEmpty()) {
            got_video();
            player = ExoPlayerFactory.newSimpleInstance(context);
            mPlayerView.setPlayer(player);
            player.setPlayWhenReady(true);
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, getActivity().getApplication().getPackageName()));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoURL));

            // Prepare the player with the source.
            player.prepare(videoSource);
        } else {
            no_video();
        }
    }

    public void SetDescriptionAdjustPlayer() {
        //Set Description
        if (recipe != null && mPlayerView != null) {
            if (isPortrait) {
                myLongDescription.setVisibility(View.VISIBLE);
                int stepIdint = Integer.parseInt(stepId) - 1;
                //Get proper description for this particular step
                myLongDescription.setText(steps.get(stepIdint).getDescription());
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                        mPlayerView.getLayoutParams();
                params.width = Math.round(getResources().getDimension(R.dimen.player_view_port_width));
                params.height = Math.round(getResources().getDimension(R.dimen.player_view_port_width)) / 2;
            } else {
                myLongDescription.setVisibility(View.INVISIBLE);
                mPlayerView.requestFocus();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                        mPlayerView.getLayoutParams();
                params.width = Math.round(getResources().getDimension(R.dimen.player_view_land_width));
                params.height = (int) Math.round(Math.round(getResources().getDimension(R.dimen.player_view_land_width)) / 2.1);
            }
        }
    }

    public void no_video() {
        mPlayerView.setVisibility(View.GONE);
    }

    public void got_video() {
        mPlayerView.setVisibility(View.VISIBLE);

    }

    private Bundle getScreenSize(){
        Bundle bundle=new Bundle();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        bundle.putString(SCREEN_W,String.valueOf(height));
        bundle.putString(SCREN_H,String.valueOf(width));
        return bundle;
    }
}
