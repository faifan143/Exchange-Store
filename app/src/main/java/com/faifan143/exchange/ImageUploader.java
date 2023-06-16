package com.faifan143.exchange;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploader {

    private FirebaseStorage mFirebaseStorage;

    public ImageUploader() {
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    public void uploadImage(Uri imageUri, final OnImageUploadListener listener) {
        StorageReference storageReference = mFirebaseStorage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = storageReference.putFile(imageUri);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.onUploadSuccess(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onUploadFailure(e.getMessage());
                        }
                    });
                } else {
                    listener.onUploadFailure(task.getException().getMessage());
                }
            }

        });
    }

    public interface OnImageUploadListener {
        void onUploadSuccess(String downloadUrl);

        void onUploadFailure(String errorMessage);
    }
}
