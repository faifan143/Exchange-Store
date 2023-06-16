package com.faifan143.exchange.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faifan143.exchange.R;
import com.faifan143.exchange.adapters.MyMessageAdapter;
import com.faifan143.exchange.models.MessageModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InChatScreen extends AppCompatActivity {
    RecyclerView myRecyclerView;
    List<MessageModel> myDataList;
    MyMessageAdapter myAdapter;
    Button sendButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPref ;
    EditText chatInputField;
    String myEmail , myNumber , myName , myFCMToken;
    TextView contactName , contactEmail ,  contactNumber;
    Map<String , Object>  contactInfo  =  new HashMap<>();
    Map<String , Object> messageJson =  new HashMap<>();
    Map<String , Object>  myInfo =  new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_chat_screen);
        sendButton = findViewById(R.id.send_button);
        chatInputField = findViewById(R.id.chat_input_field);
        contactName = findViewById(R.id.contact_name);
        contactEmail = findViewById(R.id.contact_email);
        contactNumber = findViewById(R.id.contact_number);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String number = intent.getStringExtra("number");
        String fcmToken = intent.getStringExtra("fcmToken");
        getUserData(name , email , number , fcmToken);
        contactName.setText(name);
        contactEmail.setText(email);
        contactNumber.setText(number);
        doAdapter(email);
        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(chatInputField.getText().toString().trim().length() != 0 ) {
                            MessageModel messageModel = new MessageModel(chatInputField.getText().toString(), email, number, name, System.currentTimeMillis() + "");
                            // Get a reference to the "chats" sub collection of the receiver's document in the "users" collection
                            DocumentReference myChatsDocumentRef = db.collection("users").document(myEmail).collection("chats").document(email).collection("messages").document();
                            DocumentReference contactChatsDocumentRef = db.collection("users").document(email).collection("chats").document(myEmail).collection("messages").document();
                            messageJson.put("messageBody", messageModel.getMessageBody());
                            messageJson.put("receiverEmail", messageModel.getReceiverEmail());
                            messageJson.put("receiverNumber", messageModel.getReceiverNumber());
                            messageJson.put("receiverName", messageModel.getReceiverName());
                            messageJson.put("sendTime", messageModel.getSendTime());
                            // Add the new message to the "chats" sub collection
                            myChatsDocumentRef.set(messageJson);
                            contactChatsDocumentRef.set(messageJson);

                            try {
                                sendNotificationToToken("c7ss3rY8Ty6NBhtuIIWodP:APA91bHO4zRifRHdT6LWcNSaMIffyE9ZisPinbUbg_FjiZFvmmbjrT0mgTK47_HUxv9Oh4h2SialD5fmG9b034RE6GgAuXt9KA-12Vg-w8-qfm9B5YT-yTMgWlKJ2-kWnO4yL0qHjnuK", myEmail, chatInputField.getText().toString(), null, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            chatInputField.setText("");
                            doAdapter(email);
                            // Get the number of items in the adapter

                        }
                    }
                }
        );
    }
    private void doAdapter(String receiverEmail) {
        myRecyclerView = findViewById(R.id.chat_messages_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(InChatScreen.this, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);

        myDataList = new ArrayList<>();
        myAdapter = new MyMessageAdapter(InChatScreen.this, myDataList ,myEmail);
        myRecyclerView.setAdapter(myAdapter);

        CollectionReference chatsCollectionRef = db.collection("users").document(myEmail).collection("chats").document(receiverEmail).collection("messages");

        // Listen for real-time updates
        chatsCollectionRef.orderBy("sendTime", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                myDataList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    MessageModel dataClass = documentSnapshot.toObject(MessageModel.class);
                    dataClass.setKey(documentSnapshot.getId());
                    myDataList.add(dataClass);
                }

                // Notify the adapter of the changes
                myAdapter.notifyDataSetChanged();
                int itemCount = myAdapter.getItemCount();
                myRecyclerView.scrollToPosition(itemCount - 1);
            }
        });
    }

    private void getUserData(String name , String email , String number , String fcmToken) {
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        myEmail =  sharedPref.getString("myEmail", "");
        myFCMToken = sharedPref.getString("myFCMToken","");
        DocumentReference myDocRef = db.collection("users").document(myEmail);
        Task<DocumentSnapshot> task = myDocRef.get();
        task.addOnSuccessListener(myData -> {
            if (myData.exists()) {
                while(myName==null || myNumber == null || myEmail == null){
                    myEmail = myData.getString("email");
                    myName = myData.getString("name");
                    myNumber = myData.getString("number");
                }
                contactInfo.put("name",name);
                contactInfo.put("email",email);
                contactInfo.put("number",number);
                contactInfo.put("fcmToken",fcmToken);
                db.collection("users").document(myEmail).collection("chats").document(email).set(contactInfo);
                myInfo.put("email",myEmail);
                myInfo.put("number",myNumber);
                myInfo.put("name",myName);
                myInfo.put("fcmToken",myFCMToken);
                db.collection("users").document(email).collection("chats").document(myEmail).set(myInfo);
            } else {
                // Handle case where document does not exist
                System.out.println("Document does not exist!");
            }
        }).addOnFailureListener(e -> {
            // Handle exception
            System.err.println("Error getting document: " + e.getMessage());
        });
    }

    public void sendNotificationToToken(String userToken, String title, String message, String iconUrl, String action) {
        new Thread(() -> {
            try {
                // Create URL object
                URL url = new URL("https://fcm.googleapis.com/fcm/send");

                // Create connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "key=AAAAmkKL_p8:APA91bG0YIkBYwuO9C2yY3Q4RJWRBXk1Sa8UDvDQCgYqR3wxmMgLxMXtWtgkX47Oig9oX_xwJVteSfUBu8j0t4Lt8hMeIB1ECdet1i_m5Fhh1r53TGMQM2Bs2JbW-3dpVY7KWo9tkO13");

                // Create JSON object
                JSONObject json = new JSONObject();
                json.put("to", userToken);

                JSONObject data = new JSONObject();
                data.put("title", title);
                data.put("body", message);
                if (iconUrl != null) {
                    data.put("icon", iconUrl);
                }
                if (action != null) {
                    data.put("click_action", action);
                }
                json.put("data", data);

                // Send request
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                // Get response
                int responseCode = conn.getResponseCode();
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print response
                System.out.println(response.toString());
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error sending FCM notification: " + e.getMessage());
            } catch (JSONException e) {
                System.err.println("Error creating JSON object: " + e.getMessage());
            } catch (NetworkOnMainThreadException e) {
                System.err.println("Error sending FCM notification on main thread: " + e.getMessage());
            }
        }).start();
    }


}
