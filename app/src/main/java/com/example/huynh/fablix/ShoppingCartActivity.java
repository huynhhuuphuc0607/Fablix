package com.example.huynh.fablix;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class ShoppingCartActivity extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private RecyclerView itemRecyclerView;
    private TextView totalPriceTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        itemAdapter = new ItemAdapter(this,R.layout.item_one_row,MovieListActivity.cart.getMap());
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(itemAdapter);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        totalPriceTextView.setText(format.format(itemAdapter.getTotalPrice()));

    }

    /**
     *Hypothetically finish the transaction
     * @param v the view the user clicks on to finish the transaction
     */
    public void payNow(View v)
    {
        new AlertDialog.Builder(ShoppingCartActivity.this).setTitle("Congrats")
                .setMessage("Your order is on the way!!!")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MovieListActivity.cart.clear();
                        ShoppingCartActivity.this.finish();
                    }
                }).create().show();
    }
}
