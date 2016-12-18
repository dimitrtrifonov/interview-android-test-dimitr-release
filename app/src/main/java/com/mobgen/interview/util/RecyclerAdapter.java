package com.mobgen.interview.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobgen.interview.R;
import com.mobgen.interview.model.Car;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LenovoY700 on 12/16/2016.
 */

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Car> cars;
    private Context context;

    public interface OnRecyclerViewItemClickListener{
        void onRecyclerViewItemClick(int index);
    }
    OnRecyclerViewItemClickListener onItemClickListener;

    Handler handler = new Handler();
    List<MyViewHolder> myViewHolders= new ArrayList<MyViewHolder>();
    int[] currentImage;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            for(int v=0;v<myViewHolders.size();v++){ //all items' view holders saved in a list
                myViewHolders.get(v).updateImage(v,currentImage[v]); //set new image
                currentImage[v]++;
                if(currentImage[v]==cars.get(v).getImages().size()){ //if the last image has been displayed
                    currentImage[v]=0; //next time show the first one
                }
            }

            handler.postDelayed(this,3000);//call the runnable again in 3 seconds
        }
    };

    public RecyclerAdapter(Context context,List<Car> carsList) {
        this.context = context;
        onItemClickListener = (OnRecyclerViewItemClickListener) context;
        this.cars = carsList;
        currentImage = new int[cars.size()];//save the index of currently displayed image for every car
        handler.postDelayed(runnable,0);//call the loop displaying new picture for every item/car every 3 seconds
    }


    public class MyViewHolder extends RecyclerView.ViewHolder { //view holder pattern
        public TextView title, owner, date;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);

            Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
            imageView = (ImageView) view.findViewById(R.id.imageHolder);
            imageView.setImageDrawable(transparentDrawable); //to avoid displaying ic_launcher icon for every item before loading images
            title = (TextView) view.findViewById(R.id.titleHolder);
            owner = (TextView) view.findViewById(R.id.ownerHolder);
            date = (TextView) view.findViewById(R.id.dateHolder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onRecyclerViewItemClick(getLayoutPosition());
                }
            });
        }

        String imageName;
        public void updateImage(int viewHolderIndex,int pictureIndex){ //updating item picture
            imageName=cars.get(viewHolderIndex).getImages().get(pictureIndex);

            Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            imageView.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() { //animate the update with fade
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.setImageDrawable(getDrawableFromAssets(imageName));
                    Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    imageView.startAnimation(fadeIn);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_tile, parent, false);

        MyViewHolder myViewHolder=new MyViewHolder(itemView);
        myViewHolders.add(myViewHolder); //save every item's view holder in a list
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) { //set car to view holder
        Car car = cars.get(position);
        holder.title.setText(car.getTitle());
        holder.owner.setText(car.getOwner());
        holder.date.setText(unixTimeStampToSimpleDateFormat(car.getDate()));
        }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    Drawable getDrawableFromAssets(String jpg){ //get jpg from assets and return drawable
        Drawable d = null;
        try {
            InputStream ims = this.context.getAssets().open(jpg);
            d = Drawable.createFromStream(ims, null);
            return d;
        }
        catch(IOException ex) {
            return d;
        }
    }

    String unixTimeStampToSimpleDateFormat(String timestamp){ //convert timestamp to simple date
        long dv = Long.valueOf(timestamp);
        Date df = new java.util.Date(dv);
        return new SimpleDateFormat("MMM dd, yyyy hh:mma").format(df);
    }
}
