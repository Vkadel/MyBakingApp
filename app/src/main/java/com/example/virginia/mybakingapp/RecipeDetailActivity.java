package com.example.virginia.mybakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */

public class RecipeDetailActivity extends AppCompatActivity {
    private boolean mTwoPane = false;
    RecipeViewModel viewModel;
    static String thisItemID;
    private CollapsingToolbarLayout appBarLayout;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        appBarLayout= (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        if (findViewById(R.id.step_frame_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        final String addonText=getResources().getString(R.string.this_are_steps_for);
        final View recyclerView = findViewById(R.id.step_list);
        thisItemID = getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_ID);
        final Bundle savedInstanceStateFinal = savedInstanceState;
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        //Preparing the data
         viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {

                if (savedInstanceStateFinal == null) {
                    // Create the detail fragment and add it to the activity
                    // using a fragment transaction.
                    //Create and update Fragment
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeDetailFragment.ARG_ITEM_ID,thisItemID);
                    fragment.setArguments(arguments);
                    //Removed the first Item in the Recipe

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.recipe_detail_container, fragment)
                            .commit();
                    if (mTwoPane) {
                        Bundle argumentsStep=new Bundle();
                        RecipeStepsListFragment fragmentSteps = new RecipeStepsListFragment();
                        argumentsStep.putString(RecipeStepsListFragment.ARG_ITEM_ID,thisItemID);
                        argumentsStep.putString(RecipeStepsListFragment.ARG_STEP_ID, "1");
                        fragmentSteps.setArguments(argumentsStep);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.step_detail_container, fragmentSteps).commit();
                    }
                }
                    final View recyclerView = findViewById(R.id.step_list);
                    assert recyclerView != null;
                    setupRecyclerView((RecyclerView) recyclerView, recipes.get(Integer.parseInt(thisItemID) - 1).getSteps());
                if (appBarLayout != null) {
                    appBarLayout.setTitle(addonText+" "+recipes.get(Integer.parseInt(thisItemID)-1).getName());
                }

            }


        });

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //

            NavUtils.navigateUpTo(this, new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView, ArrayList<RecipeStep> Recipes) {
        recyclerView.setAdapter(new RecipeDetailActivity.SimpleItemRecyclerViewAdapter(this, Recipes, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeDetailActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeDetailActivity mParentActivity;
        private final ArrayList<RecipeStep> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecipeStep stepItem = mValues.get(Integer.parseInt(view.getTag().toString()));
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeStepsListFragment.ARG_ITEM_ID, thisItemID);
                    String stepId=((Integer.parseInt(stepItem.getId()))+1)+"";
                    arguments.putString(RecipeStepsListFragment.ARG_STEP_ID,stepId);
                    RecipeStepsListFragment fragment = new RecipeStepsListFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    String stepId=(Integer.parseInt(stepItem.getId())+1)+"";
                    intent.putExtra(RecipeStepsListFragment.ARG_ITEM_ID, thisItemID);
                    intent.putExtra(RecipeStepsListFragment.ARG_STEP_ID, stepId);
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeDetailActivity parent,
                                      ArrayList<RecipeStep> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;

        }

        @Override
        public RecipeDetailActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_item, parent, false);
            return new RecipeDetailActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecipeDetailActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

            holder.mShortDescription.setText(mValues.get(position).getShortDescription());
            holder.itemView.setTag(mValues.get(position).getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (mValues == null) {
                return 0;
            } else {
                return mValues.size();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mShortDescription;

            ViewHolder(View view) {
                super(view);
                mShortDescription = (TextView) view.findViewById(R.id.short_description);
            }
        }
    }
}
