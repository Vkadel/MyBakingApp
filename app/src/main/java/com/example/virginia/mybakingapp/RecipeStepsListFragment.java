package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_STEP_ID = "step_id";
    public static final String ARG_ITEMS="items";

    /**
     * The dummy content this fragment is presenting.
     */

    private ArrayList<Recipe> recipes;

    private CollapsingToolbarLayout appBarLayout;
    private String stepId;
    private String itemId;
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
            appBarLayout= (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            itemId=getArguments().getString(ARG_ITEM_ID);
            stepId=getArguments().getString(ARG_STEP_ID);
            viewModel=ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Only get the recipe and set the Views if the system has data

            Recipe recipe=viewModel.getRecipes().getValue().get(Integer.parseInt(itemId)-1);
            ArrayList<RecipeStep> recipeStepserecipeSteps = recipe.getSteps();
            //Add the name to the AppBar
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getName());
            }
            View rootView = inflater.inflate(R.layout.step_detail, container, false);

            // Show the dummy content as text in a TextView.
            if (recipe != null) {
                TextView myLongDescription=((TextView) rootView.findViewById(R.id.tv_step_description_intwopane));
                int stepIdint=Integer.parseInt(stepId)-1;
                myLongDescription
                        .setText(recipeStepserecipeSteps.get(stepIdint).getDescription());
            }

        return rootView;
    }
}
