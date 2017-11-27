package masegi.sho.mytimetable.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import masegi.sho.mytimetable.R;
import masegi.sho.mytimetable.domain.value.Library;

/**
 * Created by masegi on 2017/09/27.
 */

public class LicensesListViewAdapter extends ArrayAdapter<Library> {

    private final LayoutInflater inflater;
    private final Context context;

    public LicensesListViewAdapter(Context context, List<Library> libraries) {
        super(context, 0, libraries);

        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        ViewHolder viewHolder = null;

        if (v == null) {

            v = inflater.inflate(R.layout.item_license, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView)v.findViewById(R.id.license_name);
            viewHolder.copyRightTextView = (TextView)v.findViewById(R.id.license_copy_right);
            viewHolder.licenseTextView = (TextView)v.findViewById(R.id.license_content);
            v.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder)v.getTag();
        }

        Library library = super.getItem(position);

        viewHolder.nameTextView.setText(library.getName());
        viewHolder.copyRightTextView.setText(library.getCopyRight());
        viewHolder.licenseTextView.setText(
                library.getLicense().getContentString(context));

        return v;
    }


    static class ViewHolder {

        TextView nameTextView;
        TextView copyRightTextView;
        TextView licenseTextView;
    }
}
