package com.example.virginia.mybakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */

public class RecipeStepDetailActivity extends AppCompatActivity {
    private boolean mTwoPane = false;

    static String thisItemID;
    static String thisStepID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        final Bundle savedInstanceStateFinal = savedInstanceState;
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        thisItemID = getIntent().getStringExtra(RecipeStepsListFragment.ARG_ITEM_ID);
        thisStepID =getIntent().getStringExtra(RecipeStepsListFragment.ARG_ITEM_ID);
        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {

                if (savedInstanceStateFinal == null) {
                    // Create the detail fragment and add it to the activity
                    // using a fragment transaction.
                    Bundle argumentsStep = new Bundle();

                    RecipeStepsPortraitFragment fragmentSteps = new RecipeStepsPortraitFragment();
                    argumentsStep.putString(RecipeStepsPortraitFragment.ARG_ITEM_ID, thisItemID);
                    argumentsStep.putString(RecipeStepsPortraitFragment.ARG_STEP_ID, thisStepID);
                    fragmentSteps.setArguments(argumentsStep);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_detail_container_portrait, fragmentSteps).commit();
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
           //Ensures the App goeas back to the Deatil activity on the same recipe
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, thisItemID);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, thisItemID);
            NavUtils.navigateUpTo(this, new Intent(intent));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
