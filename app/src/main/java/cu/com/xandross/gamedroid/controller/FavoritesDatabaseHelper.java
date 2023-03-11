package cu.com.xandross.gamedroid.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cu.com.xandross.gamedroid.R;
import cu.com.xandross.gamedroid.model.FavoriteGame;
import cu.com.xandross.gamedroid.model.Game;

/**
 * Created by XandrOSS on 11/05/2016.
 */
public class FavoritesDatabaseHelper extends OrmLiteSqliteOpenHelper {
  //  private static final String DATABASE_NAME = "/mnt/sdcard/GameDroid/favorites.db";
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<FavoriteGame, Integer> favoriteGameDao = null;

    // we do this so there is only one helper
    private static FavoritesDatabaseHelper helper = null;
    private static final AtomicInteger usageCounter = new AtomicInteger(0);

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call to this method, there should be 1 and only 1
     * call to {@link #close()}.
     */
    public static synchronized FavoritesDatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new FavoritesDatabaseHelper(context);
        }
        usageCounter.incrementAndGet();
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, FavoriteGame.class);

        } catch (SQLException e) {
            Log.d(DatabaseHelper.class.getName(), "Error creando la base de datos de favoritos", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, FavoriteGame.class, true);
            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.d(DatabaseHelper.class.getName(),
                    "Error actualizando la base de datos de favoritos de la versión " + oldVersion +
                            " a la " + newVersion, e);
        }
    }

    public Dao<FavoriteGame, Integer> getFavoriteGameDao() throws SQLException {
        if (favoriteGameDao == null) {
            favoriteGameDao = getDao(FavoriteGame.class);
        }
        return favoriteGameDao;
    }

    /**
     * Close the database connections and clear any cached DAOs. For each call to {@link #getHelper(Context)}, there
     * should be 1 and only 1 call to this method. If there were 3 calls to {@link #getHelper(Context)} then on the 3rd
     * call to this method, the helper and the underlying database connections will be closed.
     */
    @Override
    public void close() {
        if (usageCounter.decrementAndGet() == 0) {
            super.close();
            favoriteGameDao = null;
            helper = null;
        }
    }

    public boolean deleteFavorite(Integer gameId) throws SQLException {
        try {
            final Dao<FavoriteGame, Integer> deleteFavoriteGame = getFavoriteGameDao();
          //  deleteFavoriteGame.deleteById(gameId);
          DeleteBuilder<FavoriteGame, Integer> db = deleteFavoriteGame.deleteBuilder();
            db.where().eq(FavoriteGame.ID_GAME_FIELD_NAME, gameId);
            db.delete();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ! isFavorite(gameId);
    }

    public boolean addFavorite(Game game)  throws SQLException {

        FavoriteGame favoriteGame = new FavoriteGame(game.getId(),
                game.getStatus(), game.getShortName(),
                game.getFullName(), game.getPlatform(), game.getDate(),
                game.getKind(), game.getDistributor(), game.getDeveloper(),
                game.getFormat(), game.getSpaceOnDisk(), game.getAge(),
                game.getLanguage(), game.getDescription(), game.getRequirements(),
                game.getDiscs(), game.isLastReleased(), game.isComingSoon(),
                game.getImageBytes());

        try {
            if (! isFavorite(favoriteGame.getIdGame()))
                getFavoriteGameDao().create(favoriteGame);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isFavorite(game.getId());
    }

    public boolean isFavorite(Integer gameId) throws SQLException {

        List<FavoriteGame> favoritesGamesList;

        Dao<FavoriteGame, Integer> favoriteGameDao = getFavoriteGameDao();

        favoritesGamesList = favoriteGameDao.query(
                favoriteGameDao.queryBuilder()
                        .where()
                        .eq(FavoriteGame.ID_GAME_FIELD_NAME, gameId)
                        .prepare());

        return (favoritesGamesList.size() != 0);
    }
}
