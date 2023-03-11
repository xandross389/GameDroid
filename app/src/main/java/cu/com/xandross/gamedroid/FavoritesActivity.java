package cu.com.xandross.gamedroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cu.com.xandross.gamedroid.controller.FavoritesDatabaseHelper;
import cu.com.xandross.gamedroid.model.FavoriteGame;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesDatabaseHelper favoritesDatabaseHelper = null;
    private RecyclerView recViewFavorites;
    private List<FavoriteGame> favoritesGamesList;
    private RecyclerView.Adapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Favoritos");
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
    }

    private void cargarFavoritos() {
        /* // creo un objeto Game para insertar
        Game newGame = new Game();
        newGame.setDistributor("Ubisoft Quebec");
        newGame.setSpaceOnDisk("25 GB");
        newGame.setShortName("Assassin's Creed: Syndicate");
        newGame.setFullName("Assassin's Creed: Syndicate");
        newGame.setAge("16+");
        newGame.setComingSoon(false);
        newGame.setDeveloper("Ubisoft");
        newGame.setDiscs(1);
        newGame.setFormat("DVD");
        newGame.setKind("Acción-Aventura");
        newGame.setLanguage("Español");
        newGame.setLastReleased(true);
        newGame.setDescription("Tremenda muela");
        newGame.setRequirements("Otra muela mas larga todavia");
        newGame.setPlatform("Play Station 4, Windows, Xbox One");
        newGame.setStatus("Completado");

        // inserto algunos registros en la BD
        try {
          final Dao<Game, Integer> insertGame = getHelper().getGameDao();

            insertGame.create(newGame);

        } catch (SQLException e) {
            e.printStackTrace();
        }
*/

        // cargar los juegos estrenados recientemente desde la BD
        try {
            final Dao<FavoriteGame, Integer> favoriteGame = getFavoritesDatabaseHelper().getFavoriteGameDao();

            favoritesGamesList = favoriteGame.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {

        cargarFavoritos();

        //Inicialización RecyclerView
        recViewFavorites = (RecyclerView) findViewById(R.id.recViewFavorites);
        adaptador = new FavoritosAdapter(getApplicationContext(),
                        (ArrayList<FavoriteGame>) favoritesGamesList);
        recViewFavorites.setAdapter(adaptador);

        //Lista VERTICAL
        recViewFavorites.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Grid de 3 elementos
       // recViewPremieres.setLayoutManager(new GridLayoutManager(this,2));
        recViewFavorites.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (favoritesDatabaseHelper != null) {
            favoritesDatabaseHelper.close();
            favoritesDatabaseHelper = null;
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private FavoritesDatabaseHelper getFavoritesDatabaseHelper() {
        if (favoritesDatabaseHelper == null) {
            favoritesDatabaseHelper =
                    FavoritesDatabaseHelper.getHelper(this);
        }
        return favoritesDatabaseHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((FavoritosAdapter)  adaptador).setOnItemClickListener(new GameItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("GAME_ROW_ID", favoritesGamesList.get(position).getIdGame());
                v.getContext().startActivity(detailsIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(searchIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
