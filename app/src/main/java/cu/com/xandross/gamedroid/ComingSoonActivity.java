package cu.com.xandross.gamedroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cu.com.xandross.gamedroid.controller.DatabaseHelper;
import cu.com.xandross.gamedroid.model.Game;

public class ComingSoonActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper = null;
    private RecyclerView recViewNextPremieres;
    private RecyclerView.Adapter adaptador;
    private List<Game> nextPremiereGamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Próximos estrenos");

        initRecyclerView();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        cargarJuegosPorEstrenarProximamente();

        //Inicialización RecyclerView
        recViewNextPremieres = (RecyclerView) findViewById(R.id.recViewNextPremieres);
        adaptador = new ProximosEstrenosAdapter(
                getApplicationContext(), (ArrayList<Game>) nextPremiereGamesList);
        recViewNextPremieres.setAdapter(adaptador);

        //Lista VERTICAL
//        recView.setLayoutManager(
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Grid de 2 elementos
        recViewNextPremieres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recViewNextPremieres.setItemAnimator(new DefaultItemAnimator());
    }

    private void cargarJuegosPorEstrenarProximamente() {
        // cargar los juegos estrenados recientemente desde la BD
        try {
            final Dao<Game, Integer> game = getHelper().getGameDao();

            nextPremiereGamesList = game.query(
                    game.queryBuilder()
                            .where()
                            .eq(Game.COMING_SOON_FIELD_NAME, true)
                            .prepare());

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    @Override
    protected void onResume() {
        super.onResume();

        ((ProximosEstrenosAdapter) adaptador).setOnItemClickListener(new GameItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("GAME_ROW_ID", nextPremiereGamesList.get(position).getId());
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
