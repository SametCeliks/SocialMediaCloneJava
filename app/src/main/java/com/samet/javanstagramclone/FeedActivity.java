package com.samet.javanstagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.samet.javanstagramclone.adapter.PostAdapter;
import com.samet.javanstagramclone.databinding.ActivityFeedBinding;
import com.samet.javanstagramclone.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post>postArrayList;

    private ActivityFeedBinding binding;
    PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFeedBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        postArrayList=new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);
    }






    private void getData(){

        //DocumentReference documentReference=firebaseFirestore.collection("Posts").document("jdjdjjd");


        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    Toast.makeText(FeedActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();


                }
                if (value !=null){
                   for (DocumentSnapshot snapshot: value.getDocuments()){

                       Map<String,Object> data = snapshot.getData();
                       //Casting
                      String email= (String) data.get("email");
                      String comment =(String)data.get("comment");
                      String dowloadurl=(String) data.get("dowloadurl");


                       Post post = new Post(email,comment,dowloadurl);
                       postArrayList.add(post);


                    }

                     postAdapter.notifyDataSetChanged();

                }

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //upload
        if (item.getItemId()==R.id.add_post){

            Intent intentUpload=new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intentUpload);

        } else if (item.getItemId()==R.id.signout) {
            //signout

            auth.signOut();

            Intent intentToMain= new Intent(FeedActivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}