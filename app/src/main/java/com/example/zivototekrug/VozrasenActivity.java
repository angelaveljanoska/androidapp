package com.example.zivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.zivototekrug.utility.FirebaseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VozrasenActivity extends AppCompatActivity {

    private FirebaseUtils firebaseUtils = new FirebaseUtils();
    private FirebaseAuth mAuth;
    private String TAG = "vozrasen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vozrasen);
        mAuth = FirebaseAuth.getInstance();
        getRequestsByRequesterId();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getRequestsByRequesterId() {
        FirebaseUser user = mAuth.getCurrentUser();
        firebaseUtils.firestoreDatabase.collection("activities")
                .whereEqualTo("creatorId", user.getUid())
                .get()
                .addOnSuccessListener(
                        this, new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(@NonNull QuerySnapshot data) {
                                GridLayout activities = findViewById(R.id.activities);
                                ArrayList<TextView> textViews = new ArrayList<TextView>();
                                int i = 0;
                                while (i < data.getDocuments().size()) {
                                    TextView textView = new TextView(VozrasenActivity.this);
                                    DocumentSnapshot activity = data.getDocuments().get(i);

                                    HashMap mlad = (HashMap<String, String>) activity.getData().get("mlad");
                                    String phone = "";
                                    String name = "";
                                    if (mlad != null) {
                                        phone = mlad.get("phone").toString();
                                        name = mlad.get("name").toString();
                                    }
                                    String highPrio = (Boolean) activity.getData().get("priority") ? "Yes" : "No";
                                    textView.setText("Activity: " + activity.getData().get("name") + "\nDescription: " + activity.getData().get("description") + "\nUrgent: " + highPrio + "\nStatus: " + activity.getData().get("status") + "\nAccepted by: " + name + "\nContact phone: " + phone);
                                    textView.setTextSize(20F);
                                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    if(i%2!=0) {
                                        textView.setBackgroundColor(Color.parseColor("#6a8894"));
                                        textView.setTextColor(Color.parseColor("#ffffff"));
                                    }
                                    else {
                                        textView.setBackgroundColor(Color.parseColor("#163440"));
                                        textView.setTextColor(Color.parseColor("#ffffff"));
                                    }
                                    textViews.add(textView);
                                    activities.addView(textView);
                                    i++;
                                }
                            }
                        }
                );
    }

    public void addActivity(View v) {
        startActivity(new Intent(VozrasenActivity.this, VozrasenNewActivity.class));
    }
}