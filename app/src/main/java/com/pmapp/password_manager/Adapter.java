package com.pmapp.password_manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    String uname;
    List<String> passName, passPass;
    LayoutInflater inflater;
    Context ctxx;

    public Adapter(Context ctx, List<String> passName) {
        this.passName = passName;
        this.inflater = LayoutInflater.from(ctx);
        ctxx = ctx;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public List<String> getPassPass() {
        return passPass;
    }

    public void setPassPass(List<String> passPass) {
        this.passPass = passPass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(passName.get(position));
    }

    @Override
    public int getItemCount() {
        return passName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    passPass = getPassPass();
                    Intent intent = new Intent(ctxx, showPass.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("name",passName.get(pos));
                    intent.putExtra("pass",passPass.get(pos));
                    intent.putExtra("uname",getUname());
                    ctxx.startActivity(intent);
                    Toast.makeText(v.getContext(), "password= "+ passPass.get(pos), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
