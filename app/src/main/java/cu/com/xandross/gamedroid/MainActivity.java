package cu.com.xandross.gamedroid;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cu.com.xandross.gamedroid.controller.DatabaseHelper;
import cu.com.xandross.gamedroid.controller.FavoritesDatabaseHelper;
import cu.com.xandross.gamedroid.model.Game;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper databaseHelper = null;
    private FavoritesDatabaseHelper favoritesDatabaseHelper = null;
    private RecyclerView recViewPremieres;
    private List<Game> premiereGamesList;
    private RecyclerView.Adapter adaptador;
    private Button btnVerProximosEstrenos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        }); */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnVerProximosEstrenos = (Button) findViewById(R.id.btnVerProximosEstrenos);

//        YoYo.with(Techniques.ZoomIn)
//                .duration(2200)
//                .playOn(btnVerProximosEstrenos);
//
//        YoYo.with(Techniques.RotateOut)
//                .duration(2200)
//                .playOn(btnVerProximosEstrenos);

        YoYo.with(Techniques.StandUp)
                .duration(900)
                .playOn(btnVerProximosEstrenos);


        btnVerProximosEstrenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent estrenosActivity = new Intent(getApplicationContext(), ComingSoonActivity.class);
               // Intent estrenosActivity = new Intent(getApplicationContext(), ScrollingActivity.class);
                startActivity(estrenosActivity);
            }
        });


        initRecyclerView();
    }

    private void cargarJuegosEstrenadosRecientemente() {
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
          final Dao<Game, Integer> insertGame = getDatabaseHelper().getGameDao();

            insertGame.create(newGame);

        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
        // cargar los juegos estrenados recientemente desde la BD
        try {
            final Dao<Game, Integer> game = getDatabaseHelper().getGameDao();

            premiereGamesList = game.query(
                    game.queryBuilder()
                            .where()
                            .eq(Game.LAST_RELEASED_FIELD_NAME, true)
                            .prepare());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {

        cargarJuegosEstrenadosRecientemente();

        //Inicialización RecyclerView
        recViewPremieres = (RecyclerView) findViewById(R.id.recViewPremieres);
        adaptador = new EstrenosAdapter(getApplicationContext(), (ArrayList<Game>) premiereGamesList);
        recViewPremieres.setAdapter(adaptador);

        //Lista VERTICAL
//        recView.setLayoutManager(
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Grid de 3 elementos
        recViewPremieres.setLayoutManager(new GridLayoutManager(this,2));
        recViewPremieres.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            showSearchActivity(getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {

        } else if (id == R.id.nav_next) {

            btnVerProximosEstrenos.performClick();

        } else if (id == R.id.nav_favorites) {

            showFavoritesActivity(getApplicationContext());

        } else if (id == R.id.nav_search) {

            showSearchActivity(getApplicationContext());

        /*} else if (id == R.id.nav_share) {*/

        } else if (id == R.id.nav_contact) {

            showContactActivity(getApplicationContext());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSearchActivity(Context mContext) {
        Intent searchIntent = new Intent(mContext, SearchActivity.class);
        startActivity(searchIntent);
    }

    private void showContactActivity(Context mContext) {
        Intent contactIntent = new Intent(mContext, ContactActivity.class);
        startActivity(contactIntent);
    }

    private void showFavoritesActivity(Context mContext) {
        Intent favoritesIntent = new Intent(mContext, FavoritesActivity.class);
        startActivity(favoritesIntent);
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
    private DatabaseHelper getDatabaseHelper() {
        OpenHelperManager.releaseHelper();

        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    private FavoritesDatabaseHelper getFavoritesDatabaseHelper() {
        OpenHelperManager.releaseHelper();

        if (favoritesDatabaseHelper == null) {
            favoritesDatabaseHelper =
                    OpenHelperManager.getHelper(this, FavoritesDatabaseHelper.class);
        }
        return favoritesDatabaseHelper;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((EstrenosAdapter)  adaptador).setOnItemClickListener(new GameItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                    Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                    detailsIntent.putExtra("GAME_ROW_ID", premiereGamesList.get(position).getId());
                    v.getContext().startActivity(detailsIntent);
            }
        });
    }
}
