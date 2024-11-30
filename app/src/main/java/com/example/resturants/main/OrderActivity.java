package com.example.resturants.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.resturants.R;
import com.example.resturants.model.Order;
import com.example.resturants.model.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private ImageView imageOrder, imageMinusOrder, imageAddOrder;
    private TextView tvOrderName, tvOrderPrice, tvOrderQuantity;
    private Button btnOrderSubmit;

    private int quantity = 1; // Default quantity
    private double pricePerUnit = 0.0; // Default price

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialiser les vues
        imageOrder = findViewById(R.id.imageOrder);
        tvOrderName = findViewById(R.id.tvOrderName);
        tvOrderPrice = findViewById(R.id.tvOrderPrice);
        tvOrderQuantity = findViewById(R.id.tvOrderQuantity);
        imageMinusOrder = findViewById(R.id.imageMinusOrder);
        imageAddOrder = findViewById(R.id.imageAddOrder);
        btnOrderSubmit = findViewById(R.id.btnOrderSubmit);

        // Récupérer les données de l'Intent
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String name = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price", 0.0); // Valeur par défaut 0.0

        Log.d("OrderActivity", "Received Data - Image: " + imageUrl +
                ", Name: " + name + ", Price: " + price);

        // Valider les données
        if (imageUrl == null || name == null) {
            Toast.makeText(this, "Data is missing!", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité si les données sont invalides
            return;
        }

        // Afficher les données dans les vues
        Picasso.get().load(imageUrl).into(imageOrder);
        tvOrderName.setText(name);
        tvOrderPrice.setText(String.format("$%.2f", price)); // Formater le prix en chaîne
        tvOrderQuantity.setText(String.valueOf(quantity));

        // Gérer les clics sur les boutons pour ajuster la quantité
        imageMinusOrder.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvOrderQuantity.setText(String.valueOf(quantity));
                updatePriceDisplay(quantity,price);
            }
        });

        imageAddOrder.setOnClickListener(v -> {
            quantity++;
            tvOrderQuantity.setText(String.valueOf(quantity));
            updatePriceDisplay(quantity,price);
        });

        // Gérer le clic sur le bouton de soumission de commande
        btnOrderSubmit.setOnClickListener(v -> {
            // Log pour vérifier le comportement
            Log.d("OrderActivity", "Submitting order...");

            // Créer la liste des articles
            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem(name, quantity, price)); // Ajoutez ici d'autres éléments de la commande

            // Calculer le prix total
            double totalPrice = quantity * price;

            // Obtenir l'heure actuelle
            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

            // Créer l'objet Order
            Order order = new Order(items, totalPrice, timestamp, "pending");

            // Obtenir l'ID de l'utilisateur connecté
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid(); // ID de l'utilisateur connecté

                // Référence à la base de données Firebase
                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(userId);

                // Ajouter la commande
                String orderId = ordersRef.push().getKey(); // Générer un ID unique pour la commande
                ordersRef.child(orderId).setValue(order).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Log pour vérifier si la commande a été ajoutée avec succès
                        Log.d("OrderActivity", "Order added successfully.");
                        Toast.makeText(this, "Order successfully placed!", Toast.LENGTH_SHORT).show();

                        // Ouvrir l'historique des commandes
                        Intent intent = new Intent(OrderActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to place order.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    // Update the price display based on quantity
    private void updatePriceDisplay(int quantity,double pricePerUnit) {
        double totalPrice = quantity * pricePerUnit;
        tvOrderPrice.setText("$"+totalPrice);
    }
}
