package com.example.virginia.mybakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class recipe_widget extends AppWidgetProvider {
static String myIngredientsText;

static String myItemValue;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.recipe_name, myItemValue);
        views.setTextViewText(R.id.appwidget_ingredients_list,myIngredientsText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.my_widget_recipe_id),Context.MODE_PRIVATE);
            String defaultValue = context.getResources().getString(R.string.my_widget_recipe_id_default_value);
            String defaultValueIngredients=context.getResources().getString(R.string.my_widget_ingredients_default_value);
            myItemValue = sharedPref.getString(context.getString(R.string.my_widget_recipe_id), defaultValue);
            myIngredientsText=sharedPref.getString(context.getString(R.string.my_widget_recipe_ingredients_id), defaultValue);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

