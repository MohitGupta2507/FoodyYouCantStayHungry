package com.foodies.mohitgupta.foodyyoucantstsyhungry;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Mohit Gupta on 24-03-2018.
 */

public class AdapterImageSlider extends PagerAdapter {
    private int imgs[]={R.drawable.food1,R.drawable.food2,R.drawable.food33};
    private LayoutInflater inflater;
    private Context ctx;

    public AdapterImageSlider( Context ctx){
        this.ctx=ctx;
    }
    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem( ViewGroup container, int position) {

        inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.images,container,false);
        ImageView img=v.findViewById(R.id.tt);
        img.setImageResource(imgs[position]);
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }

}
