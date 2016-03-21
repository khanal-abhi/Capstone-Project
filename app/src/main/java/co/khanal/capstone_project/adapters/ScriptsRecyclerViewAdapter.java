package co.khanal.capstone_project.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.khanal.capstone_project.R;
import co.khanal.capstone_project.utililty.Script;

/**
 * Created by abhi on 3/21/16.
 */
public class ScriptsRecyclerViewAdapter
        extends RecyclerView.Adapter<ScriptsRecyclerViewAdapter.ScriptViewHolder> {

    List<Script> scripts;
    Context context;
    OnItemClickListener listener;

    public ScriptsRecyclerViewAdapter(List<Script> scripts, OnItemClickListener listener){
        this.scripts = scripts;
        this.listener = listener;
    }

    @Override
    public ScriptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.script_card_view, parent, false);
        return new ScriptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScriptViewHolder holder, int position) {
        holder.bind(scripts.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }


    public static class ScriptViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView subtitle;
        public CardView scriptCard;

        public ScriptViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            subtitle = (TextView)itemView.findViewById(R.id.subtitle);
            scriptCard = (CardView)itemView.findViewById(R.id.script_card);
        }

        public void bind(final Script script, final OnItemClickListener listener){
            title.setText(script.getFileName());
            String subtitle = "";
            try {
                subtitle = script.getContent().substring(0, 50);
            } catch (ArrayIndexOutOfBoundsException e){
                subtitle = script.getContent();
            } finally {
                subtitle += itemView.getContext().getString(R.string.ellipsis);
            }
            this.subtitle.setText(subtitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(script);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Script script);
    }
}
