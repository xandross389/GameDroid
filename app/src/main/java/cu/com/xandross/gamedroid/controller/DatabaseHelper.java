package cu.com.xandross.gamedroid.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cu.com.xandross.gamedroid.R;
import cu.com.xandross.gamedroid.model.FavoriteGame;
import cu.com.xandross.gamedroid.model.Game;


/**
 * Created by XandrOSS on 11/03/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // private static final String DATABASE_NAME = "gamedroid.db";
    //"/sdcard/mydatabase.db"
    private static final String DATABASE_NAME = "/mnt/sdcard/GameDroid/gamedroid.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Game, Integer> gameDao = null;
    private static final AtomicInteger usageCounter = new AtomicInteger(0);

    // we do this so there is only one helper
    private static DatabaseHelper helper = null;

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call to this method, there should be 1 and only 1
     * call to {@link #close()}.
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        usageCounter.incrementAndGet();
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Game.class);

        } catch (SQLException e) {
            Log.d(DatabaseHelper.class.getName(), "Error creando la base de datos", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Game.class, true);
            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.d(DatabaseHelper.class.getName(),
                    "Error actualizando la base de datos de la versión " + oldVersion +
                    " a la " + newVersion, e);
        }
    }

    public Dao<Game, Integer> getGameDao() throws SQLException {
        if (gameDao == null) {
            gameDao = getDao(Game.class);
        }
        return gameDao;
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
            gameDao = null;
            helper = null;
        }
    }

}
