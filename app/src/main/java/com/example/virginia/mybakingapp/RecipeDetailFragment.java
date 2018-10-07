package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEMS="items";
    public static final String FIRST_STEP = "0";
    private Recipe mItem;
    private ArrayList<Recipe> Recipes;
    private CollapsingToolbarLayout appBarLayout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }
    RecipeViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            Activity activity = this.getActivity();
            appBarLayout= (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            viewModel=ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
            Recipes= viewModel.getRecipes().getValue();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

           Timber.e("Inflating detail Fragment");
            mItem = Recipes.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID))-1);
            //Add the name to the AppBar
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
            View rootView = inflater.inflate(R.layout.recipe_detail_ingredients, container, false);

            if (mItem != null) {
                ArrayList<RecipeIngredient> ingredientsArray=mItem.getIngredients();
                String mylistofIngredients="Ingredient List\n\n";
                for(int i=0;i<ingredientsArray.size();i++){
                    RecipeIngredient ingredient;
                    ingredient=ingredientsArray.get(i);
                    mylistofIngredients=mylistofIngredients+ingredient.getQuantity()+" ";
                    mylistofIngredients=mylistofIngredients+ingredient.getMeasure()+" ";
                    mylistofIngredients=mylistofIngredients+ingredient.getIngredient()+"\n";
                }

                ((TextView) rootView.findViewById(R.id.recipe_detail_ingredients)).setText(mylistofIngredients);
                ((TextView) rootView.findViewById(R.id.servings)).setText("for "+ mItem.servings+" "+
                        getActivity().getResources().getString(R.string.word_servings));
            }
        return rootView;
    }


}
