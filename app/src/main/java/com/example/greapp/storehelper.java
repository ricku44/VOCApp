package com.example.greapp;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;


class storehelper {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String sr = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private Map<String,Object> updatedb;

    void updatestore(Context context) {
        QuestionHelper dbhelper= new QuestionHelper(context);
        dbhelper.prepareDatabase();
        Map<String,String> user=dbhelper.getRatio();

        db.collection(sr).document("ratio").set(user);
    }

    void updateSQL(Context context) {
        QuestionHelper dbhelper = new QuestionHelper(context);
        dbhelper.prepareDatabase();

        db.collection(sr).document("ratio").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    updatedb = document.getData();
                    assert updatedb != null;
                    try {
                        String s = updatedb.get("44").toString();
                        for (Map.Entry entry : updatedb.entrySet()) {
                            int id = Integer.parseInt(entry.getKey().toString());

                            String obj = entry.getValue().toString();

                            String[] rw = obj.split(" ");

                            dbhelper.questionUpdate(Integer.parseInt(rw[0]), Integer.parseInt(rw[1]), id);
                            Log.e("rr", "NEW" + id + Integer.parseInt(rw[0]) + Integer.parseInt(rw[1]));
                        }
                    } catch (Exception e){}

                }
            }
        });


    }



}

