package net.eazypg.janmashtami;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HomePageActivity  extends AppCompatActivity {

    ImageView backButton;
    Button counterButton, addButton;
    TextView  daysLueftTextView;
    EditText counterEditText;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String count;
    Date date , date1;
    String datevalue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        backButton = findViewById(R.id.backButton);
        counterButton = findViewById(R.id.count);
        addButton = findViewById(R.id.addButton);
        daysLueftTextView = findViewById(R.id.daysLeftTextView);
        counterEditText = findViewById(R.id.counterEditText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Folks/" + firebaseUser.getUid());
        date = new Date();
        String pattern;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        try {
            date1 =  sdf.parse("23/08/2019");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = (date1.getTime() - date.getTime());

        daysLueftTextView.setText((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))+"");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.child("count").getValue(String.class);
                counterButton.setText(count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counterEditText.getText().toString().isEmpty()) {
                    counterEditText.setError("Required");
                }
                else {

                    databaseReference.child("count").setValue(Integer.parseInt(count) + Integer.parseInt(counterEditText.getText().toString()));
                }
            }
        });

    }
}
