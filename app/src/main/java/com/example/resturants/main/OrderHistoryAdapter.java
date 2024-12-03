package com.example.resturants.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturants.R;
import com.example.resturants.model.Order;
import com.example.resturants.model.OrderItem;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderDate.setText(order.getTimestamp());
        holder.tvOrderTotalPrice.setText(String.format("$%.2f", order.getTotalPrice()));

        StringBuilder itemsText = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsText.append(item.getName()).append(" x").append(item.getQuantity()).append("\n");
        }
        holder.tvOrderItems.setText(itemsText.toString());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderDate, tvOrderTotalPrice, tvOrderItems;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotalPrice = itemView.findViewById(R.id.tvOrderTotalPrice);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
        }
    }

}

