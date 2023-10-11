package com.sliit.budgetplanner.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.PersistentCacheSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBUtil {
    private static FBUtil instance = null;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    private FBUtil() {
        firestore = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder(firestore.getFirestoreSettings())
                        .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                        .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
                        .build();

        firestore.setFirestoreSettings(settings);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
    }

    public static FBUtil getInstance() {
        if (instance == null)
            instance = new FBUtil();

        return instance;
    }

    public FirebaseFirestore getDB() {
        return firestore;
    }

    public FirebaseAuth getAuth() {
        return firebaseAuth;
    }

    public FirebaseStorage getStorage() {
        return firebaseStorage;
    }

    public StorageReference getBucketRef() {
        return firebaseStorage.getReferenceFromUrl("gs://em-budgetplanner.appspot.com");
    }
}
