package com.example.hanfood.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.AdminEditDeleteFoodActivity;
import com.example.hanfood.AdminFoodActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.HomeViewHolder> {
    private List<Cart> list;
    Context context;
    private DatabaseReference myRef;
    private FirebaseUser auth;
    int totalQuantity = 0;

    public CartAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Cart cart = list.get(position);

        holder.product_name.setText(cart.getProductName());
        holder.tvSl.setText(String.valueOf(cart.getTotalQuantity()));
        if (cart.getProductPriceSalse() < cart.getProductPrice()) {
            holder.product_price.setText(decimalFormat.format(cart.getProductPrice()) + " VNĐ");
            holder.product_price.setPaintFlags(holder.product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.product_priceSale.setText(decimalFormat.format(cart.getProductPriceSalse()) + " VNĐ");

        } else {
            holder.product_price.setVisibility(View.GONE);
            holder.product_priceSale.setText(decimalFormat.format(cart.getProductPrice()) + " VNĐ");
        }

        Picasso.get().load(cart.getProductImg())
                .into(holder.img);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalQuantity = list.get(position).getTotalQuantity();
                if (totalQuantity < 10) {
                    totalQuantity++;
                    holder.tvSl.setText(new StringBuilder().append(totalQuantity));
                    saveData(position);
                }
            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalQuantity = list.get(position).getTotalQuantity();
                if (totalQuantity > 0) {
                    totalQuantity--;
                    holder.tvSl.setText(String.valueOf(totalQuantity));
                    saveData(position);
                }
                if (totalQuantity == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa món ăn");
                    builder.setMessage("Bạn có chắc muốn xóa món ăn này?");
                    builder.setIcon(R.drawable.remove);
                    builder.setNegativeButton("Bỏ qua", null);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myRef.child(cart.getProductName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    list.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, list.size());
                                    Toast.makeText(context.getApplicationContext(),
                                            "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Cart/" + auth.getUid());
        holder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xóa món ăn");
                builder.setMessage("Bạn có chắc muốn xóa món ăn này?");
                builder.setIcon(R.drawable.remove);
                builder.setNegativeButton("Bỏ qua", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child(cart.getProductName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, list.size());
                                Toast.makeText(context.getApplicationContext(),
                                        "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void saveData(int position) {
        double price;
        if (list.get(position).getProductPrice() > list.get(position).getProductPriceSalse()) {
            price = list.get(position).getProductPriceSalse();
        } else price = list.get(position).getProductPrice();
        HashMap<String, Object> food = new HashMap<>();
        food.put("totalQuantity", totalQuantity);
        food.put("totalPrice", totalQuantity * price);
        myRef.child(list.get(position).getProductName()).updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Update quantity cart");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img, sub, add;
        TextView product_name, product_price, tvSl, product_priceSale;
        Button clear;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.product_img);
            add = view.findViewById(R.id.add);
            sub = view.findViewById(R.id.sub);
            product_name = view.findViewById(R.id.product_name);
            tvSl = view.findViewById(R.id.tvSl);
            product_price = view.findViewById(R.id.product_price);
            product_priceSale = view.findViewById(R.id.product_priceSale);
            clear = view.findViewById(R.id.btClear);
            cardView = view.findViewById(R.id.cardView);
        }

    }

}
