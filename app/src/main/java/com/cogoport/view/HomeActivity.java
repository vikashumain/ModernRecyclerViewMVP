package com.cogoport.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cogoport.MvpContract.RxjavaRetrofitcontract;
import com.cogoport.R;
import com.cogoport.adapter.AdapterExample;
import com.cogoport.app.AppController;
import com.cogoport.drawer.DrawerPresenterImpl;
import com.cogoport.model.Album;
import com.cogoport.presenter.DataPresenterImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class HomeActivity extends AppCompatActivity implements RxjavaRetrofitcontract.MvpViewApi{

    private static final String TAG = HomeActivity.class.getName();
    DataPresenterImpl picturePresenter;
    ArrayList<Album> list;
     RecyclerView recyclerView;

    Toolbar toolbar;

    ProgressBar progressBar;
    RecyclerView.Adapter adapter;

    RequestQueue queue;

    private DrawerPresenterImpl drawerPresenter;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        list=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_views);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        progressBar=(ProgressBar)findViewById(R.id.toolbar);
//        picturePresenter = new PicturePresenterImpl();
//        picturePresenter.attachedView2(HomeActivity.this);
        setupViews();
        setupRecyclerView();
        queue= AppController.getInstance().getRequestQueue();
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        preparealbums();

        //        navigationView.getMenu().performIdentifierAction(R.id.nav_linear_h, 0);
    }



    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showMessage(String message) {
        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void setupViews() {
        setSupportActionBar(toolbar);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    @Override
    public void onResume() {

        super.onResume();
    }
    public void onItemClickListener(int position) {
        picturePresenter.onItemSelected(position);
    }
    public  void preparealbums()
    {

//        if(adapter instanceof AdapterExample)
//
//            ((AdapterExample) adapter).setRecyclerItemClickListener((RecyclerItemClickListener) HomeActivity.this);

        String url="http://starlord.hackerearth.com/studio";


        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.d(TAG, response.toString());


                        // Parsing(manipulating) json because data we are getting before it is unstructured so we will get them
                        //using perfect key value pairs
                        for (int i = 0; i < response.length(); i++) {
                            try
                            {
                                JSONObject obj = response.getJSONObject(i);
                                Album movie = new Album(obj.getString("song"),obj.getString("url"),
                                        obj.getString("artists"),obj.getString("cover_image"));




                           /*     JSONArray genreArry = obj.getJSONArray("genre");
                                ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);
                             */   list.add(movie);

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });

        // Adding request to request queue
        queue.add(movieReq);

    }



    protected RecyclerView.LayoutManager getLayoutManager() {
        return getGridLayoutManager();
    }

    protected RecyclerView.Adapter getAdapter() {
        return new AdapterExample(getApplicationContext(),R.layout.item_type_two,list);
    }

    private GridLayoutManager getGridLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                HomeActivity.this,
                2,
                GridLayoutManager.VERTICAL,
                false);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //stagger rows custom
                return (position % 3 == 0 ? 2 : 1);
            }
        });


        return gridLayoutManager;
    }










    private void setupRecyclerView() {
        if(getLayoutManager() != null)
            recyclerView.setLayoutManager(getLayoutManager());
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
