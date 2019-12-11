package com.example.imgur.features.posts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.imgur.R;
import com.example.imgur.data.Images;
import com.example.imgur.data.ImagesData;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

  private List<ImagesData> ListOfImages;
  private Context context;

  public RecyclerAdapter(List<ImagesData> listOfImages, Context context) {
    this.ListOfImages = listOfImages;
    this.context = context;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.gridlayoutview_item, viewGroup, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int i) {
    final ImagesData image = ListOfImages.get(i);

    holder.Title.setText(image.getTitle());
    Picasso.with(context).load(image.getLink()).into(holder.Image);

    holder.CardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ImageFullScreenActivity.class);
        intent.putExtra("Title", image.getTitle());
        intent.putExtra("Desc", image.getDesc());
        intent.putExtra("URL", image.getLink());
        context.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return ListOfImages.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView Image;
    public TextView Title;
    public CardView CardView;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      Title = (TextView) itemView.findViewById(R.id.ImageTitle);

      Image = (ImageView) itemView.findViewById(R.id.ImageURl);
      CardView = (CardView) itemView.findViewById(R.id.CardViewId);
    }
  }

  public void addImages(List<ImagesData> images) {
    for (ImagesData i : images) {
      ListOfImages.add(i);
    }

    notifyDataSetChanged();
  }
}
