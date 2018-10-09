package com.example.virginia.mybakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */

public class RecipeStepDetailActivity extends AppCompatActivity {
    RecipeViewModel viewModel;
    static String thisItemID;
    static String thisStepID;
    long thisPlayerPosition;
    RecipeStepsPortraitFragment fragmentSteps;
    RecipeStepsLandscapeFragment fragmentStepsLand;
    Context context;
    Boolean isPortrait;
    Boolean isLandscapeAndSmall;
    long playerPosition=0;
    private static final String SCREN_H="screen_h";
    private static final String SCREEN_W="screen_w";
    public static final String ARG_PLAYER_POSITION = "player_position";
    RecipeStepsLandscapeFragment newfragmentStepsLand;
    RecipeStepsPortraitFragment newfragmentSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        context=this;
        TextView landScapeSmallContainer=findViewById(R.id.land_scape_tv);
        if(landScapeSmallContainer!=null){
            isLandscapeAndSmall=true;
        }else{
            isLandscapeAndSmall=false;
        }
        //Check if activity is in portrait mode
        if (this.getResources().getConfiguration().orientation ==
                Resources.getSystem().getConfiguration().ORIENTATION_PORTRAIT) {
            isPortrait = true;
        } else {
            isPortrait = false;
        }

        final Bundle savedInstanceStateFinal = savedInstanceState;
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        boolean mTwoPane = false;
        if (actionBar != null && (mTwoPane || isPortrait)) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null && (!mTwoPane && !isPortrait)) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            AppBarLayout bar = findViewById(R.id.app_bar);
            bar.setVisibility(View.GONE);
        }

        //Sent the activity to a detail landscape view if in landscape and
        //and larger than 600
        final int screenSize=Integer.parseInt(getScreenSize().getString(SCREEN_W));
        final int orientation = getResources().getConfiguration().orientation;

        thisItemID = getIntent().getStringExtra(RecipeStepsListFragment.ARG_ITEM_ID);
        thisStepID = getIntent().getStringExtra(RecipeStepsListFragment.ARG_STEP_ID);
        if(savedInstanceState!=null){
            thisItemID = savedInstanceState.getString(RecipeStepsListFragment.ARG_ITEM_ID);
            thisStepID = savedInstanceState.getString(RecipeStepsListFragment.ARG_STEP_ID);
        }
        viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Recipe> recipes) {
                if (savedInstanceStateFinal == null) {
                    // Create the detail fragment and add it to the activity
                    // using a fragment transaction.
                    if (isPortrait) {
                        Bundle argumentsStep = new Bundle();
                        fragmentSteps = new RecipeStepsPortraitFragment();
                        argumentsStep.putString(RecipeStepsPortraitFragment.ARG_ITEM_ID, thisItemID);
                        argumentsStep.putString(RecipeStepsPortraitFragment.ARG_STEP_ID, thisStepID);
                        argumentsStep.putBoolean(RecipeStepsPortraitFragment.ARG_IS_PORTRAIT, isPortrait);
                        fragmentSteps.setArguments(argumentsStep);
                       getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container_portrait, fragmentSteps).commit();
                    }
                    if (isLandscapeAndSmall) {
                        Bundle argumentsStep = new Bundle();
                        fragmentStepsLand = new RecipeStepsLandscapeFragment();
                        argumentsStep.putString(RecipeStepsLandscapeFragment.ARG_ITEM_ID, thisItemID);
                        argumentsStep.putString(RecipeStepsLandscapeFragment.ARG_STEP_ID, thisStepID);
                        argumentsStep.putBoolean(RecipeStepsLandscapeFragment.ARG_IS_LANDSCAPE_AND_SMALL, isLandscapeAndSmall);
                        fragmentStepsLand.setArguments(argumentsStep);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.step_detail_container_portrait, fragmentStepsLand).commit();}

                }
            }
        });

        if(savedInstanceState!=null){
            if(isLandscapeAndSmall){
                if(isLandscapeAndSmall){
                    boolean isNew=false;
                    Bundle argumentsStepLand = new Bundle();
                    newfragmentStepsLand = new RecipeStepsLandscapeFragment();
                    argumentsStepLand.putString(RecipeStepsLandscapeFragment.ARG_ITEM_ID, thisItemID);
                    argumentsStepLand.putString(RecipeStepsLandscapeFragment.ARG_STEP_ID, thisStepID);
                    argumentsStepLand.putBoolean(RecipeStepsLandscapeFragment.ARG_IS_LANDSCAPE_AND_SMALL, isLandscapeAndSmall);
                    argumentsStepLand.putLong(ARG_PLAYER_POSITION,savedInstanceState.getLong(ARG_PLAYER_POSITION));
                    newfragmentStepsLand.setArguments(argumentsStepLand);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container_portrait, newfragmentStepsLand).commit();
                }
            }
            if (isPortrait){
                newfragmentStepsLand=null;
                Bundle argumentsStep = new Bundle();
                newfragmentSteps = new RecipeStepsPortraitFragment();
                argumentsStep.putString(RecipeStepsPortraitFragment.ARG_ITEM_ID, thisItemID);
                argumentsStep.putString(RecipeStepsPortraitFragment.ARG_STEP_ID, thisStepID);
                argumentsStep.putBoolean(RecipeStepsPortraitFragment.ARG_IS_PORTRAIT, isPortrait);
                if (savedInstanceState.containsKey(ARG_PLAYER_POSITION)){
                argumentsStep.putLong(ARG_PLAYER_POSITION,savedInstanceState.getLong(ARG_PLAYER_POSITION));}
                //Send
                if(thisPlayerPosition!=0){
                    argumentsStep.putLong(ARG_PLAYER_POSITION,thisPlayerPosition);
                }
                newfragmentSteps.setArguments(argumentsStep);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container_portrait, newfragmentSteps).commit();
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //Ensures the App goeas back to the Detail activity on the same recipe
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, thisItemID);
            NavUtils.navigateUpTo(this, new Intent(intent));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(fragmentStepsLand!=null){
            thisPlayerPosition=fragmentStepsLand.getPlayerPosition();
        }
        if(fragmentSteps!=null){
            thisPlayerPosition=fragmentSteps.getPlayerPosition();
        }
        if(newfragmentSteps!=null){
            thisPlayerPosition=newfragmentSteps.getPlayerPosition();
        }
        if (newfragmentStepsLand!=null){
            thisPlayerPosition=newfragmentStepsLand.getPlayerPosition();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        thisItemID = savedInstanceState.getString(RecipeStepsPortraitFragment.ARG_ITEM_ID);
        thisStepID = savedInstanceState.getString(RecipeStepsPortraitFragment.ARG_STEP_ID);
        if (savedInstanceState.containsKey(ARG_PLAYER_POSITION)){
            thisPlayerPosition=savedInstanceState.getLong(ARG_PLAYER_POSITION);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RecipeStepsPortraitFragment.ARG_ITEM_ID,thisItemID);
        outState.putString(RecipeStepsPortraitFragment.ARG_STEP_ID,thisStepID);

        //Getting previous player position if the screen is turned
        if(fragmentStepsLand!=null){
             playerPosition=fragmentStepsLand.getPlayerPosition();
        }
        if(fragmentSteps!=null){
             playerPosition=fragmentSteps.getPlayerPosition();
        }
        if(newfragmentSteps!=null){
            playerPosition=newfragmentSteps.getPlayerPosition();
        }
        if (newfragmentStepsLand!=null){
            playerPosition=newfragmentStepsLand.getPlayerPosition();
        }

        outState.putLong(ARG_PLAYER_POSITION,playerPosition);
        super.onSaveInstanceState(outState);

    }

    private Bundle getScreenSize(){
        Bundle bundle=new Bundle();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        bundle.putString(SCREEN_W,String.valueOf(height));
        bundle.putString(SCREN_H,String.valueOf(width));
        return bundle;
    }



}
