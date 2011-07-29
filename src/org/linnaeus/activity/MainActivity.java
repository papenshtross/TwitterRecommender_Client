package org.linnaeus.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.android.maps.*;
import org.linnaeus.R;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.facebook.FacebookConstants;
import org.linnaeus.facebook.FacebookFactory;
import org.linnaeus.manager.AdviceManager;
import org.linnaeus.manager.RequestManager;
import org.linnaeus.overlay.CurrentLocationOverlay;
import org.linnaeus.overlay.SearchCircleOverlay;
import org.linnaeus.util.MainActivityContext;
import org.linnaeus.util.MyLocation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MapActivity {

    private static final String CATEGORY_KEY_PREFIX = "cat";
    private static final String CATEGORIES_IMPORTED = "imported";

    Facebook facebook = new Facebook(FacebookConstants.FACEBOOK_APP_ID);

    private static boolean IS_IMPORT_FROM_FB;

    private static final int CURRENT_LOCATION_ZOOM_LEVEL = 10;

    public static final int SEARCH_DIALOG = 1;
    public static final int ADVICE_DIALOG = 2;
    public static final int PROGRESS_DIALOG = 3;
    public static final int KEYWORD_DIALOG = 4;
    public static final int CATEGORY_DIALOG = 5;
    public static final int IMPORT_FB_CATEGORIES_DIALOG = 6;
    public static final int PREFERENCES_DIALOG = 7;
    public static final int CONFIRM_DELETE_DIALOG = 8;

    public final int REQUEST_TRENDS = 1;
    public final int REQUEST_ADVICE = 2;

    private MapController mapController;
    private MyLocation myLocation = new MyLocation();
    private MyLocation.LocationResult locationResult;
    private MapView mapView;
    private SearchCircleOverlay searchCircleOverlay;
    private int currentCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //Init context to allow static access throughout the application
        MainActivityContext.getInstance().initContext(this);

        mapView = (MapView) findViewById(R.id.mapmain);
        mapView.setBuiltInZoomControls(true); // Attach zoom control
        /* Give start condition (location/zoom) */
        mapController = mapView.getController();

        locationResult = new MyLocation.LocationResult() {
            public void gotLocation(final Location location) {
                if (location != null) {
                    double lat = location.getLatitude() * 1000000;
                    double lng = location.getLongitude() * 1000000;
                    GeoPoint geoPoint = new GeoPoint((int) lat, (int) lng);
                    MyLocation.setLastLocation(geoPoint);
                    mapController.animateTo(geoPoint);
                    mapController.setZoom(CURRENT_LOCATION_ZOOM_LEVEL);
                }
            };
        };

        CurrentLocationOverlay myLocationOverlay = new CurrentLocationOverlay(getApplicationContext());
        List<Overlay> list = mapView.getOverlays();
        list.add(myLocationOverlay);

        facebook.authorize(this, new Facebook.DialogListener() {
            @Override
            public void onComplete(Bundle values) {
            }

            @Override
            public void onFacebookError(FacebookError error) {
            }

            @Override
            public void onError(DialogError e) {
            }

            @Override
            public void onCancel() {
            }
        });
        FacebookFactory.getInstance().initFacebook(facebook);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.my_location:
                moveToMyLocation();
                return true;
            case R.id.help:
                showHelp();
                return true;
            case R.id.circle:
                drawCircle();
                return true;
            case R.id.pref:
                openPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openPreferences() {
        showDialog(PREFERENCES_DIALOG);
    }

    private void drawCircle() {
        mapView.getOverlays().remove(searchCircleOverlay);
        searchCircleOverlay = new SearchCircleOverlay(this);
        mapView.getOverlays().add(searchCircleOverlay);
    }

    private void moveToMyLocation() {
        try {
            if (!myLocation.getLocation(this, locationResult)) {
                showErrorLocationToast(getString(R.string.main_error_services));
            }
        } catch (Exception e) {
            showErrorLocationToast(getString(R.string.main_error_mylocation));
        }
    }

    private void showErrorLocationToast(String text) {
        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    private void showHelp() {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override // Required by MapActivity
    protected boolean isRouteDisplayed() {
        return false;
    }

    public MapView getMapView() {
        return mapView;
    }

    @Override
    public Dialog onCreateDialog(int id) {
        switch(id) {
            case (SEARCH_DIALOG) :
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle(getString(R.string.search_circle_dialog_title));
                ad.setMessage(getString(R.string.search_circle_dialog_message));
                ad.setPositiveButton(getString(R.string.search_circle_dialog_button_trends),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                requestTrends();
                            }
                        });
                ad.setNegativeButton(getString(R.string.search_circle_dialog_button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                cancelSearch();
                            }
                        });
                ad.setNeutralButton(getString(R.string.search_circle_dialog_button_recommend),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                showDialog(ADVICE_DIALOG);
                            }
                        });
                return ad.create();
            case (ADVICE_DIALOG) :
                AlertDialog.Builder adviceDialog = new AlertDialog.Builder(this);
                adviceDialog.setTitle(getString(R.string.main_advice_dialog_title));
                adviceDialog.setMessage(getString(R.string.main_advice_dialog_text));
                adviceDialog.setPositiveButton(getString(R.string.main_advice_dialog_keyword_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                showDialog(KEYWORD_DIALOG);
                            }
                        });
                adviceDialog.setNegativeButton(getString(R.string.main_advice_dialog_category_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                if (isCategoriesImported()){
                                    showDialog(CATEGORY_DIALOG);
                                } else {
                                    showDialog(IMPORT_FB_CATEGORIES_DIALOG);
                                }
                            }
                        });
                return adviceDialog.create();
            case (PROGRESS_DIALOG) :
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle(getString(R.string.main_progress_dialog_title));
                dialog.setMessage(getString(R.string.main_progress_dialog_text));
                dialog.show();
                return dialog;
            case (KEYWORD_DIALOG) :
                AlertDialog.Builder keywordDialog = new AlertDialog.Builder(this);
                keywordDialog.setTitle(getString(R.string.main_keyword_dialog_title));
                final EditText input = new EditText(this);
                keywordDialog.setView(input);
                keywordDialog.setPositiveButton(getString(R.string.main_keyword_dialog_request_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                String value = input.getText().toString();
                                requestRecommendation(value);
                            }
                        });
                keywordDialog.setNegativeButton(getString(R.string.main_keyword_dialog_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                cancelSearch();
                            }
                        });
                return keywordDialog.create();
            case (CATEGORY_DIALOG) :
                AlertDialog.Builder categoryDialog = new AlertDialog.Builder(this);
                categoryDialog.setTitle(getString(R.string.main_category_dialog_title));
                final String[] categories;
                ArrayList<String> categoriesList = getCategoriesFromPreferences();
                if (categoriesList.size() > 0){
                    categories = categoriesList.toArray(new String[0]);
                } else {
                    categoriesList = AdviceManager.getInstance().getCategories(IS_IMPORT_FROM_FB);
                    putCategoriesToPreferences(categoriesList);
                    categories = categoriesList.toArray(new String[0]);
                }
                categoryDialog.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                setCurrentCategory(item);
                            }
                        });
                categoryDialog.setPositiveButton(getString(R.string.main_category_dialog_request_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                requestRecommendation(categories[currentCategory]);
                            }
                        });
                categoryDialog.setNegativeButton(getString(R.string.main_keyword_dialog_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                cancelSearch();
                            }
                        });
                return categoryDialog.create();
            case (IMPORT_FB_CATEGORIES_DIALOG) :
                AlertDialog.Builder importDialog = new AlertDialog.Builder(this);
                importDialog.setTitle(getString(R.string.main_import_dialog_title));
                importDialog.setMessage(getString(R.string.main_import_dialog_text));
                importDialog.setPositiveButton(getString(R.string.main_import_dialog_request_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                IS_IMPORT_FROM_FB = true;
                                showDialog(CATEGORY_DIALOG);
                            }
                        });
                importDialog.setNegativeButton(getString(R.string.main_import_dialog_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                IS_IMPORT_FROM_FB = false;
                                showDialog(CATEGORY_DIALOG);
                            }
                        });
                return importDialog.create();
            case (PREFERENCES_DIALOG) :
                AlertDialog.Builder prefDialog = new AlertDialog.Builder(this);
                prefDialog.setTitle(getString(R.string.pref_dialog_title));
                String[] preferences = {getString(R.string.pref_dialog_remove_categories)};
                prefDialog.setItems(preferences, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dismissDialog(PREFERENCES_DIALOG);
                                switch (item){
                                    case 0:
                                        showDialog(CONFIRM_DELETE_DIALOG);
                                        break;
                                }
                            }
                        });
                prefDialog.setNegativeButton(getString(R.string.main_category_dialog_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dismissDialog(PREFERENCES_DIALOG);
                            }
                        });
                return prefDialog.create();
            case (CONFIRM_DELETE_DIALOG) :
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
                deleteDialog.setTitle(getString(R.string.delete_dialog_title));
                deleteDialog.setMessage(getString(R.string.delete_dialog_text));
                deleteDialog.setPositiveButton(getString(R.string.delete_dialog_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                clearSharedPreferences();
                            }
                        });
                deleteDialog.setNegativeButton(getString(R.string.delete_dialog_no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dismissDialog(CONFIRM_DELETE_DIALOG);
                            }
                        });
                return deleteDialog.create();
        }
        return null;
    }

    private void cancelSearch() {
        removeSearchOverlay();
    }

    private void requestRecommendation(String value) {
        new SearchRequest().execute(String.valueOf(REQUEST_ADVICE), value);
        removeSearchOverlay();
    }

    private void requestTrends() {
        new SearchRequest().execute(String.valueOf(REQUEST_TRENDS));
        removeSearchOverlay();
    }

    private void removeSearchOverlay() {
        dismissDialog(SEARCH_DIALOG);
        getMapView().getOverlays().remove(searchCircleOverlay);
    }

    class SearchRequest extends AsyncTask<String, String, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@Override
		protected Void doInBackground(String... request) {
            SearchCircle searchCircle = searchCircleOverlay.getSearchCircle();
            switch (Integer.valueOf(request[0])){
                case REQUEST_TRENDS:
                    RequestManager.getInstance().requestTrends(searchCircle);
                    break;
                case REQUEST_ADVICE:
                    RequestManager.getInstance().requestRecommendation(searchCircle, request[1]);
                    break;
            }
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			dismissDialog(PROGRESS_DIALOG);
		}
	}

    public int getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(int currentCategory) {
        this.currentCategory = currentCategory;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    private void putCategoriesToPreferences(ArrayList<String> categories){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < categories.size(); i++){
            editor.putString(CATEGORY_KEY_PREFIX + i, categories.get(i));
        }
        editor.putBoolean(CATEGORIES_IMPORTED, true);
        editor.commit();
    }

    private ArrayList<String> getCategoriesFromPreferences(){
        ArrayList<String> categories = new ArrayList<String>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int i = 0;
        String key = CATEGORY_KEY_PREFIX + i;
        while (preferences.contains(key)){
            categories.add(preferences.getString(key, ""));
            i++;
            key = CATEGORY_KEY_PREFIX + i;
        }
        return categories;
    }

    private boolean isCategoriesImported(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(CATEGORIES_IMPORTED, false);
    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
