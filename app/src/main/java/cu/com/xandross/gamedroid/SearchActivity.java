package cu.com.xandross.gamedroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cu.com.xandross.gamedroid.controller.DatabaseHelper;
import cu.com.xandross.gamedroid.model.Game;

public class SearchActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper = null;
    private RecyclerView recViewSearchResults;
    private SearchResultsAdapter adaptador;
    private List<Game> searchResultGameList;
    private TextView txtResultadosHeader;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Buscar juego");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResultGameList = new ArrayList<>();

        initViewObjects();
        initRecyclerView();
    }

    private void initViewObjects() {

        txtResultadosHeader = (TextView) findViewById(R.id.txtResultadosHeader);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Criterio de búsqueda...");

        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {

                    search(query);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Debe especificar un criterio a buscar",
                            Toast.LENGTH_SHORT).show();
                    txtResultadosHeader.setVisibility(View.INVISIBLE);
                    txtResultadosHeader.setText("");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {

                    search(newText);

                } else {
                    search(newText);
                    txtResultadosHeader.setVisibility(View.INVISIBLE);
                    txtResultadosHeader.setText("");
                }
                return false;
            }
        });
    }

    private void search(String pattern){
        if (pattern.length() <= 0) {

            searchResultGameList.clear();

        } else {

            try {
                final Dao<Game, Integer> gameDao = getHelper().getGameDao();
                searchResultGameList.clear();
                searchResultGameList = gameDao.query(
                        gameDao.queryBuilder().where()
                                .like(Game.SHORT_NAME_FIELD_NAME, "%" + pattern + "%")
                                .or()
                                .like(Game.FULL_NAME_FIELD_NAME, "%" + pattern + "%")
                                .or()
                                .like(Game.DECRIPTION_FIELD_NAME, "%" + pattern + "%")
                                .or()
                                .like(Game.REQUIREMENTS_FIELD_NAME, "%" + pattern + "%")
                                .or()
                                .like(Game.PLATFORMS_FIELD_NAME, "%" + pattern + "%")
                                .prepare());

                if (searchResultGameList.size() > 0) {
                    txtResultadosHeader.setVisibility(View.VISIBLE);
                    txtResultadosHeader.setText("Coincidencias (" +
                            String.valueOf(searchResultGameList.size()) + ")");
                } else {
                    //txtResultadosHeader.setVisibility(View.VISIBLE);
                    txtResultadosHeader.setVisibility(View.VISIBLE);
                    txtResultadosHeader.setText("No se encontraron resultados");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //  SearchResultsAdapter
        adaptador = (SearchResultsAdapter) recViewSearchResults.getAdapter();
        adaptador.refreshAdapterData((ArrayList<Game>) searchResultGameList);
        adaptador.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        adaptador.setOnItemClickListener(new GameItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("GAME_ROW_ID", searchResultGameList.get(position).getId());
                v.getContext().startActivity(detailsIntent);
            }
        });
    }

    private void initRecyclerView() {
        //Inicialización RecyclerView
        recViewSearchResults = (RecyclerView) findViewById(R.id.recViewSerachResults);

        adaptador = new SearchResultsAdapter(
                getApplicationContext(), (ArrayList<Game>) searchResultGameList);
        recViewSearchResults.setAdapter(adaptador);

        recViewSearchResults.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recViewSearchResults.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

}
