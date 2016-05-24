package com.example.piero.postnote1;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private List<PostItem> postList;
    private boolean colored = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo, testo, id;

        public MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.title);
            testo = (TextView) view.findViewById(R.id.testo);
            id = (TextView) view.findViewById(R.id.ID);
        }

    }

    public PostAdapter(List<PostItem> moviesList) {
        this.postList = moviesList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PostItem post = postList.get(position);
        holder.titolo.setText(post.getTitolo());
        holder.testo.setText("" + post.getTesto());
        holder.id.setText("" + post.getId());
        //holder.data.setText(post.getYear());
    }

    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<PostItem> items){
        postList = new ArrayList<>(items);
    }

    public void animateTo(List<PostItem> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PostItem> newModels) {
        for (int i = postList.size() - 1; i >= 0; i--) {
            final PostItem model = postList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PostItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PostItem model = newModels.get(i);
            if (!postList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<PostItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PostItem model = newModels.get(toPosition);
            final int fromPosition = postList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public PostItem removeItem(int position) {
        final PostItem model = postList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, PostItem model) {
        postList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final PostItem model = postList.remove(fromPosition);
        postList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

}
