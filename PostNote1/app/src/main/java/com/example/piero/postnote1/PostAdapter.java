package com.example.piero.postnote1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{
    private List<PostItem> postList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo, testo, id;

        public MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.title);
            testo = (TextView) view.findViewById(R.id.testo);
            //id = (TextView) view.findViewById(R.id.year);
        }

    }

    public PostAdapter(List<PostItem> moviesList) {
        this.postList = moviesList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostItem post = postList.get(position);
        holder.titolo.setText(post.getTitolo());
        holder.testo.setText("" + post.getTesto());
        //holder.data.setText(post.getYear());
    }


    public int getItemCount() {
        return postList.size();
    }
}
