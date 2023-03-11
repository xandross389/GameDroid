import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cu.com.xandross.gamedroid.GameItemClickListener;
import cu.com.xandross.gamedroid.R;
import cu.com.xandross.gamedroid.model.FavoriteGame;
import cu.com.xandross.gamedroid.model.Game;

/**
 * Created by XandrOSS on 15/05/2016.
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.GamesViewHolder>  {
    private Context mContext;
    private ArrayList<Game> datos;
    private static GameItemClickListener gameItemClickListener;

    public FavoritesAdapter(Context context, ArrayList<Game> datos) {
        this.datos = datos;
        mContext = context;
    }


    public static class GamesViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context vContext;
        private TextView txtGameName;
        private TextView txtDistributor;
        private ImageView imgGameThumb;

        public GamesViewHolder(Context context, View itemView) {
            super(itemView);
            vContext = context;
            itemView.setOnClickListener(this);

            txtGameName = (TextView) itemView.findViewById(R.id.txtGameName);
            txtDistributor = (TextView) itemView.findViewById(R.id.txtDistributor);
            imgGameThumb = (ImageView) itemView.findViewById(R.id.imgGameThumb);


        }

        public void bindGame(Game game) {
            txtGameName.setText(game.getShortName());
            txtDistributor.setText(game.getDistributor());

            // load image into view
            Glide.with(vContext).load(game.getImageBytes()).
                    centerCrop().into(imgGameThumb);
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
        Game item = datos.get(position);
        holder.bindGame(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

}
