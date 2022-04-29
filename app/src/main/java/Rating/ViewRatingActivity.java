package Rating;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.vmac.WatBot.MainActivity;
import com.example.vmac.WatBot.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ViewRatingActivity extends AppCompatActivity {

    Database db;
    ArrayList<String> id, name, comment, type, address;
    ArrayList<Float> rating;
    RatingAdapter ratingAdapter;
    int count;
    float ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rating);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        db = new Database(ViewRatingActivity.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        rating = new ArrayList<>();
        comment = new ArrayList<>();
        type = new ArrayList<>();
        address = new ArrayList<>();

        storeDataInArray();
        Map<String, Float> sorting = new HashMap<String, Float>();
        for(int i=0; i<name.size(); i++){
            sorting.put(name.get(i),rating.get(i));
        }
        Object[] a = sorting.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Float>) o2).getValue()
                        .compareTo(((Map.Entry<String, Float>) o1).getValue());
            }
        });
        name.clear();
        for ( String key : sorting.keySet() ) {
            name.add(key);
        }
        Collections.sort(rating, Collections.reverseOrder());
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewRatingActivity.this));
        ratingAdapter = new RatingAdapter(ViewRatingActivity.this,this,id,name,rating,comment,type,address);
        recyclerView.setAdapter(ratingAdapter);

    }

    void storeDataInArray(){
        Cursor cursor = db.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
        else{
            while (cursor.moveToNext()){

                Cursor c = db.readComment(cursor.getString(1));
                if(c.getCount()==0){
                    Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                }
                else{
                    while (c.moveToNext()){
                        count +=1;
                        ratings += c.getFloat(3);
                        System.out.println("Text：" + ratings);
                        System.out.println("Text：" + count);

                    }
                    ratings = ratings/count;

                }
                rating.add(ratings);
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                ratings = 0;
                count = 0;
                comment.add(cursor.getString(3));
                type.add(cursor.getString(4));
                address.add(cursor.getString(5));

            }

        }

    }

}