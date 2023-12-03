package com.example.eshopcommerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshopcommerce.Entity.Produto;
import com.example.eshopcommerce.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Produto> products;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);

        void onUpdateClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ProductListAdapter(List<Produto> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto product = products.get(position);

        holder.txtProductName.setText(product.getNome());

        // Remova as linhas abaixo, pois os elementos não estão no layout item_product
        // holder.txtProductDescription.setText(product.getDescricao());
        // holder.txtProductPrice.setText(String.format("R$%s", product.getPreco()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateList(List<Produto> updatedList) {
        products.clear();
        products.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        Button btnDelete, btnUpdate;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

            btnUpdate.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUpdateClick(position);
                    }
                }
            });
        }
    }
}
