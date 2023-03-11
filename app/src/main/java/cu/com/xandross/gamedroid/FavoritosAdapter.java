package cu.com.xandross.gamedroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.sql.SQLException;
import java.util.ArrayList;

import cu.com.xandross.gamedroid.controller.FavoritesDatabaseHelper;
import cu.com.xandross.gamedroid.model.FavoriteGame;

/**
 * Created by XandrOSS on 15/05/2016.
 */
public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.GamesViewHolder>  {
    private Context mContext;
    private ArrayList<FavoriteGame> datos;
    private static GameItemClickListener gameItemClickListener;

    public FavoritosAdapter(Context context, ArrayList<FavoriteGame> datos) {
        this.datos = datos;
        mContext = context;
    }


    public static class GamesViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context vContext;
        private TextView txtGameName;
        private TextView txtDistributor;
        private ImageView imgGameThumb;
        private ImageView imgGameFavorite;
        private FavoritesDatabaseHelper favoritesDatabaseHelper = null;

        public GamesViewHolder(Context context, View itemView) {
            super(itemView);
            vContext = context;

            YoYo.with(Techniques.RotateInUpLeft)
                    .duration(500)
                    .playOn(itemView);

            itemView.setOnClickListener(this);

            txtGameName = (TextView) itemView.findViewById(R.id.txtGameName);
            txtDistributor = (TextView) itemView.findViewById(R.id.txtDistributor);
            imgGameThumb = (ImageView) itemView.findViewById(R.id.imgGameThumb);
            imgGameFavorite = (ImageView) itemView.findViewById(R.id.imgGameFavorite);
        }

        // This is how, DatabaseHelper can be initialized for future use
        private FavoritesDatabaseHelper getFavoritesDatabaseHelper() {
            if (favoritesDatabaseHelper == null) {
                favoritesDatabaseHelper =
                        FavoritesDatabaseHelper.getHelper(vContext);
            }
            return favoritesDatabaseHelper;
        }

        public void bindGame(FavoriteGame game) {
            txtGameName.setText(game.getShortName());
            txtDistributor.setText(game.getDistributor());

            // load image into view
            Glide.with(vContext).load(game.getImageBytes()).
                    centerCrop().into(imgGameThumb);

            try {
                if (getFavoritesDatabaseHelper().isFavorite(game.getIdGame())) {
                    imgGameFavorite.setImageResource(R.drawable.ic_star_filled);
                } else imgGameFavorite.setImageResource(R.drawable.ic_star_empty);
            } catch (SQLException e) {
                e.printStackTrace();
                imgGameFavorite.setImageResource(R.drawable.ic_star_empty);
            }

            if (favoritesDatabaseHelper != null) {
                favoritesDatabaseHelper.close();
                favoritesDatabaseHelper = null;
            }
        }

        @Override
        public void onClick(View v) {
            gameItemClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(GameItemClickListener gameItemClickListener) {
        this.gameItemClickListener = gameItemClickListener;
    }

    @Override
    public GamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_grid_item_1, parent, false);
        GamesViewHolder gvh = new GamesViewHolder(mContext, itemView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GamesViewHolder holder, int position) {
        FavoriteGame item = datos.get(position);
        holder.bindGame(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

}