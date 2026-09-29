package com.example.smartpantry.adapter;

import static java.lang.Boolean.parseBoolean;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.ExpiryChecker;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface Listener {
        void onItemClicked(PantryItem item);
        void onDeleteClicked(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.name.setText(item.getName());

        StringBuilder subtitle = new StringBuilder();
        subtitle.append(formatQuantity(item.getQuantity())).append(" ").append(item.getUnit());
        if (!TextUtils.isEmpty(item.getExpiryDate())) {
            subtitle.append(" · expires ").append(item.getExpiryDate());
        }

        boolean alertsEnabled = Boolean.parseBoolean(DatabaseHelper.getInstance(holder.itemView.getContext()).
                getSetting("expiry_alerts_enabled", "true"));

        ExpiryChecker.Status status = alertsEnabled ? ExpiryChecker.check(item.getExpiryDate()) : ExpiryChecker.Status.NONE;

        boolean needsAttention = status == ExpiryChecker.Status.EXPIRED || status ==ExpiryChecker.Status.EXPIRING_SOON;

        if(status == ExpiryChecker.Status.EXPIRED){
            subtitle.append(" . Expired");
        }else if(status == ExpiryChecker.Status.EXPIRING_SOON){
            subtitle.append(" . Expiring Soon");
        }

        holder.quantity.setText(subtitle.toString());

        int textColor = holder.itemView.getResources().getColor(needsAttention ? R.color.missing : R.color.text_secondary);
        holder.quantity.setTextColor(textColor);

        if(holder.itemView instanceof MaterialCardView){
            int strokeColor = holder.itemView.getResources().getColor(needsAttention ? R.color.missing : R.color.outline);
            ((MaterialCardView) holder.itemView).setStrokeColor(strokeColor);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView quantity;
        final ImageButton deleteButton;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_item_name);
            quantity = itemView.findViewById(R.id.text_item_quantity);
            deleteButton = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}
