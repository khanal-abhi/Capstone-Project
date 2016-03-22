package co.khanal.capstone_project.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.khanal.capstone_project.R;
import co.khanal.capstone_project.utililty.Script;

/**
 * Created by abhi on 3/22/16.
 */
public class ScriptsCursorAdapter extends RecyclerViewCursorAdapter<ScriptsCursorAdapter.ScriptViewHolder> {

    ScriptsCursorAdapter.OnItemClickListener clickListener;
    ScriptsCursorAdapter.OnItemLongClickListener longClickListener;

    public ScriptsCursorAdapter(Context context, Cursor cursor, OnItemClickListener itemClickListener,
                                OnItemLongClickListener onItemLongClickListener) {
        super(context, cursor);
        this.clickListener = itemClickListener;
        this.longClickListener = onItemLongClickListener;
    }

    @Override
    public void onBindViewHolder(ScriptsCursorAdapter.ScriptViewHolder viewHolder, Cursor cursor) {
        Script script = Script.fromCursor(cursor);
        viewHolder.bind(script, clickListener, longClickListener);
    }

    @Override
    public ScriptsCursorAdapter.ScriptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.script_card_view, parent, false);
        return new ScriptsCursorAdapter.ScriptViewHolder(view);
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

        public void bind(final Script script, final ScriptsCursorAdapter.OnItemClickListener clickListener, final ScriptsCursorAdapter.OnItemLongClickListener longClickListener){
            title.setText(script.getFileName());
            String subtitle = "";
            try {
                subtitle = script.getContent().substring(0, 50);
            } catch (StringIndexOutOfBoundsException e){
                subtitle = script.getContent();
                subtitle += itemView.getContext().getString(R.string.ellipsis);
            }
            this.subtitle.setText(subtitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(script);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return longClickListener.onItemLongClick(script);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Script script);
    }

    public interface OnItemLongClickListener{
        boolean onItemLongClick(Script script);
    }
}
