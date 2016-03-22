package co.khanal.capstone_project.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

import co.khanal.capstone_project.ScriptsProvider;

/**
 * Created by abhi on 3/22/16
 * Referenced ths gist https://gist.github.com/skyfishjy/443b7448f59be978bc59 for this implementation
 * thanks to skyfishjy
 */
public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    private Context context;
    private Cursor cursor;
    private DataSetObserver observer;

    public RecyclerViewCursorAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {
        if(cursor != null){
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if(cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(ScriptsProvider.ID_INDEX);
        }
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(cursor == null){
            throw new IllegalStateException("Cursor is null");
        }
        if(!cursor.moveToPosition(position)){
            throw new IllegalArgumentException("Cannot move cursor to the the position: " + position);
        }
        onBindViewHolder(holder, cursor);
    }

    public Cursor swapCursor(Cursor newCursor){
        if(newCursor == cursor){
            return null;
        }

        final Cursor oldCursor = cursor;
        if(oldCursor != null && observer != null){
            oldCursor.unregisterDataSetObserver(observer);
        }
        cursor = newCursor;

        if(cursor != null){
            cursor.registerDataSetObserver(observer);
            notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor){
        Cursor old = swapCursor(cursor);
        if(old != null){
            old.close();
        }
    }

    public class NotifyingDatasetObserver extends DataSetObserver{
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChanged();
        }
    }
}
