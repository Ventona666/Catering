package Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.Restaurant;

public class RestaurantService {
    private final String URL_DATABASE = "https://catering-bdd-default-rtdb.europe-west1.firebasedatabase.app";
    private DatabaseReference databaseReference;

    public RestaurantService(){
        databaseReference = FirebaseDatabase.getInstance(URL_DATABASE).getReference("restaurants");
    }

    public Task<DataSnapshot> findAll(){
        return databaseReference.get();

    }
}
