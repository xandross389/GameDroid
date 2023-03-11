package cu.com.xandross.gamedroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cu.com.xandross.gamedroid.controller.DatabaseHelper;
import cu.com.xandross.gamedroid.controller.FavoritesDatabaseHelper;
import cu.com.xandross.gamedroid.model.FavoriteGame;
import cu.com.xandross.gamedroid.model.Game;

public class DetailsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper = null;
    private FavoritesDatabaseHelper favoritesDatabaseHelper = null;
    //private Integer gameRowID;
    private Game game;

    private FloatingActionButton fab;
    private ImageView imageviewGameDetails;
    private TextView txtStatus;
    private TextView txtDescripcion;
    private TextView txtRequerimientos;
    private TextView txtFullName;
    private TextView txtPlatform;
    private TextView txtDate;
    private TextView txtKind;
    private TextView txtDistributor;
    private TextView txtFormat;
    private TextView txtUsedSpace;
    private TextView txtAge;
    private TextView txtLenguage;
    private TextView txtDiscs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Integer gameRowID = getIntent().getExtras().getInt("GAME_ROW_ID", 0);

        initViewObjects();

        loadGameById(gameRowID);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                YoYo.with(Techniques.Tada)
                        .duration(300)
                        .playOn(fab);

                Boolean favorite = false;

                    try {
                        favorite = getFavoritesDatabaseHelper().isFavorite(game.getId());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                if (favorite) {

                    try {
                        if (getFavoritesDatabaseHelper().deleteFavorite(game.getId())) {
                            fab.setImageResource(R.drawable.ic_star_empty_bordered);

                            Snackbar.make(view, "Eliminado de favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {

                                        getFavoritesDatabaseHelper().addFavorite(game);
                                        fab.setImageResource(R.drawable.ic_star_filled_bordered);

                                    } catch (SQLException e) {
                                        e.printStackTrace();

                                        Snackbar.make(view, "Imposible deshacer",
                                                Snackbar.LENGTH_LONG)
                                                .setAction(null, null).show();
                                        fab.setImageResource(R.drawable.ic_star_empty_bordered);
                                    }
                                }
                            }).show();

                        } else {

                            fab.setImageResource(R.drawable.ic_star_filled_bordered);

                            Snackbar.make(view, "Imposible eliminar favorito",
                                    Snackbar.LENGTH_LONG)
                                    .setAction(null, null).show();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else { // si no es favorito

                    try {
                        if (getFavoritesDatabaseHelper().addFavorite(game)) {
                            fab.setImageResource(R.drawable.ic_star_filled_bordered);

                            Snackbar.make(view, "Agregado a favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {

                                        getFavoritesDatabaseHelper().deleteFavorite(game.getId());
                                        fab.setImageResource(R.drawable.ic_star_empty_bordered);

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        Snackbar.make(view, "Imposible deshacer",
                                                Snackbar.LENGTH_LONG)
                                                .setAction(null, null).show();
                                    }
                                }
                            }).show();

                        } else {
                            fab.setImageResource(R.drawable.ic_star_empty_bordered);
                            Snackbar.make(view, "Imposible agregar favorito",
                                    Snackbar.LENGTH_LONG)
                                    .setAction(null, null).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void loadGameById(Integer gameId) {
        if (gameId == 0) {
            Toast.makeText(getApplicationContext(), "no hay datos", Toast.LENGTH_LONG).show();
        } else {
            try {
                Dao<Game, Integer> gameDao = getDatabaseHelper().getGameDao();

                // obtengo el juego de la BD de favoritos
                game = gameDao.queryForId(gameId);
                // marco como favorito o no
              //  game.setFavorite(isFavorite(gameId));

                // prueba
                //   game.setFavorite(true);

                // muestro los datos del juego
                showGameData(game);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void initViewObjects() {
        // FloatingActionButton
        fab = (FloatingActionButton) findViewById(R.id.fab);

        YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(fab);


        imageviewGameDetails = (ImageView) findViewById(R.id.imageviewGameDetails);
        txtStatus = (TextView) findViewById(R.id.txtEstado);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txtRequerimientos = (TextView) findViewById(R.id.txtRequerimientos);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        txtPlatform = (TextView) findViewById(R.id.txtPlataforma);
        txtDate = (TextView) findViewById(R.id.txtFecha);
        txtKind = (TextView) findViewById(R.id.txtGenero);
        txtDistributor = (TextView) findViewById(R.id.txtDistribuidor);
        txtFormat = (TextView) findViewById(R.id.txtFormato);
        txtUsedSpace = (TextView) findViewById(R.id.txtTamanno);
        txtAge = (TextView) findViewById(R.id.txtEdad);
        txtLenguage = (TextView) findViewById(R.id.txtIdioma);
        txtDiscs = (TextView) findViewById(R.id.txtDiscos);
    }

    public void showGameData(Game game) {
        setTitle(game.getShortName());

        Glide.with(getApplicationContext()).load(game.getImageBytes()).
                centerCrop().into(imageviewGameDetails);

        txtFullName.setText(game.getFullName());
        txtStatus.setText(game.getStatus());
        txtPlatform.setText(game.getPlatform());
        txtKind.setText(game.getKind());
        txtDistributor.setText(game.getDistributor());
        txtFormat.setText(game.getFormat());
        txtUsedSpace.setText(game.getSpaceOnDisk());
        txtAge.setText(game.getAge());
        txtLenguage.setText(game.getLanguage());
        txtDiscs.setText(String.valueOf(game.getDiscs()));

        txtDescripcion.setText(game.getDescription());
        txtRequerimientos.setText(game.getRequirements());

        if (isFavorite(game.getId())) {

            fab.setImageResource(R.drawable.ic_star_filled_bordered);
        } else {
            fab.setImageResource(R.drawable.ic_star_empty_bordered);
        }
    }


    public boolean isFavorite(Integer gameId) {
        try {
            Dao<FavoriteGame, Integer> favoriteGameDao = getFavoritesDatabaseHelper().getFavoriteGameDao();

            List<FavoriteGame> favoritesGamesList;

            favoritesGamesList = favoriteGameDao.query(
                    favoriteGameDao.queryBuilder()
                            .where()
                            .eq(FavoriteGame.ID_GAME_FIELD_NAME, gameId)
                            .prepare());

            return (favoritesGamesList.size() != 0);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
            databaseHelper = null;
        }

        if (favoritesDatabaseHelper != null) {
            favoritesDatabaseHelper.close();
            favoritesDatabaseHelper = null;
        }
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    DatabaseHelper.getHelper(this);
        }
        return databaseHelper;
    }

    private FavoritesDatabaseHelper getFavoritesDatabaseHelper() {
        if (favoritesDatabaseHelper == null) {
            favoritesDatabaseHelper =
                    FavoritesDatabaseHelper.getHelper(this);
        }
        return favoritesDatabaseHelper;
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

    private void showSearchActivity(Context mContext) {
        Intent searchIntent = new Intent(mContext, SearchActivity.class);
        startActivity(searchIntent);
    }
}