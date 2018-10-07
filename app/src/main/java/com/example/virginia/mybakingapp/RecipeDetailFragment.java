package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    String mylistofIngredients;
    private ArrayList<Recipe> Recipes;
    private CollapsingToolbarLayout appBarLayout;
    @BindView(R.id.put_recipe_ingredients_in_widget)
    Button addIngredientsToWidget;
    @BindView(R.id.recipe_detail_ingredients) TextView recipeDetailstv;
    @BindView(R.id.servings) TextView servings;
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
        ButterKnife.bind(this,rootView);
            if (mItem != null) {
                ArrayList<RecipeIngredient> ingredientsArray=mItem.getIngredients();
                mylistofIngredients="Ingredient List\n\n";
                for(int i=0;i<ingredientsArray.size();i++){
                    RecipeIngredient ingredient;
                    ingredient=ingredientsArray.get(i);
                    mylistofIngredients=mylistofIngredients+ingredient.getQuantity()+" ";
                    mylistofIngredients=mylistofIngredients+ingredient.getMeasure()+" ";
                    mylistofIngredients=mylistofIngredients+ingredient.getIngredient()+"\n";
                }

                Context context=getContext();
                recipeDetailstv.setText(mylistofIngredients);
                servings.setText(getActivity().getResources().getString(R.string.word_for)+ mItem.servings+" "+
                        getActivity().getResources().getString(R.string.word_servings));
                final String myToastText=mylistofIngredients;
                addIngredientsToWidget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recipe_widget this_recipe_widget=new recipe_widget();
                        SharedPreferences sharedPref = getActivity().getSharedPreferences(getResources().
                                getString(R.string.my_widget_recipe_id),Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.my_widget_recipe_id),mItem.getName());
                        editor.putString(getString(R.string.my_widget_recipe_ingredients_id),mylistofIngredients);
                        editor.commit();

                        Toast.makeText(context,getResources().getString(R.string.updating_your_widget_with)+mItem.getName(),
                                Toast.LENGTH_SHORT).show();

                        //Calling a widget Update manually
                        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, recipe_widget.class));
                        recipe_widget myWidget = new recipe_widget();
                        myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);

                    }
                });
            }

        return rootView;
    }


}
