package com.example.virginia.mybakingapp;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ArrayList<Recipe> Recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //Planting a Tree
        Timber.plant(new Timber.DebugTree());


        final View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        //setupRecyclerView((RecyclerView) recyclerView);
        RecipeViewModel model=ViewModelProviders
                .of(this).get(RecipeViewModel.class);

        model.getRecipes().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                Recipes=recipes;
                Timber.e("I got the power!");
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView,recipes);


            }
        });




    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView,ArrayList<Recipe> Recipes) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, Recipes, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final ArrayList<Recipe> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe item = mValues.get(Integer.parseInt(view.getTag().toString())-1);
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item.id);
                    context.startActivity(intent);

            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      ArrayList<Recipe> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).name);
            holder.mServings.setText(mValues.get(position).servings);

            holder.itemView.setTag(mValues.get(position).id);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if(mValues==null){return 0;}
            else{return mValues.size();}
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mServings;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.tv_recipe_name);
                mServings = (TextView) view.findViewById(R.id.tv_recipe_servings);
            }
        }
    }
}
