package com.example.mies_dinapen.camara;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mies_dinapen.Audio.AudioListAdapter;
import com.example.mies_dinapen.Audio.TimeAgo;
import com.example.mies_dinapen.R;

import java.io.File;

public class CamaraListAdapter extends RecyclerView.Adapter<CamaraListAdapter.CamaraViewHolder> {

    //////////////Aqui los audios se visualiza en forma de lista

    private File[] allFiles;
    private TimeAgo timeAgo;

    private onItemListClick onItemListClick;

    public CamaraListAdapter(File[] allFiles, onItemListClick onItemListClick) {
        this.allFiles = allFiles;
        this.onItemListClick = onItemListClick;
    }

    @NonNull
    @Override
    public CamaraListAdapter.CamaraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_photo_item, parent, false);
        timeAgo = new TimeAgo();
        return new CamaraListAdapter.CamaraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CamaraViewHolder holder, int position) {
        holder.list_title.setText(allFiles[position].getName());

    }



    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class CamaraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title;


        public CamaraViewHolder(@NonNull View itemView) {
            super(itemView);
            list_title = itemView.findViewById(R.id.list_photo_name);
            list_image = itemView.findViewById(R.id.list_photo);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemListClick.onClickListener(allFiles[getAdapterPosition()], getAdapterPosition());
        }
    }

    public interface onItemListClick {
        void onClickListener(File file, int position);
    }

}

