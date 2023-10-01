package com.sliit.budgetplanner.util;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.PersistentCacheSettings;

public class FBUtil {
    private static FBUtil instance = null;
    private FirebaseFirestore firestore;

    private FBUtil() {
        firestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder(firestore.getFirestoreSettings())
                        .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                        .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                        .build();

        firestore.setFirestoreSettings(settings);
    }

    public static FBUtil getInstance() {
        if (instance == null)
            instance = new FBUtil();

        return instance;
    }

    public FirebaseFirestore getDB() {
        return firestore;
    }
}
