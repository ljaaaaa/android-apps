package lilja.kiiski.bottlecapbanker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomAdapter extends BaseAdapter {
    public Context context;
    public int images[];
    public LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] images) {
        this.context = applicationContext;
        this.images = images;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        icon.setImageResource(images[i]);
        return view;
    }
}
