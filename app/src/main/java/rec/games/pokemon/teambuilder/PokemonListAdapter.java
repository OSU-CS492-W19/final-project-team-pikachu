package rec.games.pokemon.teambuilder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonViewHolder> {
    private List<Pokemon> mPokemon;
    private OnPokemonClickListener mListener;

    PokemonListAdapter(List<Pokemon> pokemon, OnPokemonClickListener l) {
        this.mPokemon = pokemon;
        this.mListener = l;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View v = inf.inflate(R.layout.pokemon_list_entry, parent, false);
        return new PokemonViewHolder(v, mListener);
    }

    @Override
    public int getItemCount() {
        return mPokemon.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder viewHolder, int i) {
        viewHolder.bind(mPokemon.get(i));
    }
}

class PokemonViewHolder extends RecyclerView.ViewHolder {
    OnPokemonClickListener mListener;
    private TextView mName;
    private ImageView mIcon;

    public PokemonViewHolder(View view, OnPokemonClickListener l) {
        super(view);
        mName = view.findViewById(R.id.pokemon_name);
        mIcon = view.findViewById(R.id.pokemon_icon);
        mListener = l;

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mListener.onPokemonClicked(getAdapterPosition());
            }
        });
    }

    public void bind(Pokemon p) {
        mName.setText(p.identifier);
        // TODO: set icon....
    }
}

interface OnPokemonClickListener {
    void onPokemonClicked(int position);
}