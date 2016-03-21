package co.khanal.capstone_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import co.khanal.capstone_project.R;
import co.khanal.capstone_project.utililty.Script;

/**
 * Created by abhi on 3/20/16.
 */
public class ScriptAdapter extends ArrayAdapter<Script> {

    Context context;
    int layoutId;
    Script[] scripts;

    public ScriptAdapter(Context context, int resource, Script[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.scripts = objects;
    }

    @Override
    public int getCount() {
        return scripts.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if(row == null){
            // need to create a new row and inflate it and then attach a holder to it using setTag()
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.subTitle = (TextView)row.findViewById(R.id.subtitle);

            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        holder.title.setText(scripts[position].getFileName());
        String subtitle = "";
        try {
            subtitle = scripts[position].getContent().substring(0, 50);
        } catch (ArrayIndexOutOfBoundsException e){
            subtitle = scripts[position].getContent();
        } finally {
            subtitle += getContext().getString(R.string.ellipsis);
        }
        holder.subTitle.setText(subtitle);

        return row;
    }

    public class ViewHolder{
        public TextView title;
        public TextView subTitle;
    }
}
