package fr.heban.todoheban;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> entries = new ArrayList<String>();

    private final ActivityResultLauncher<Intent> strGetResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent res = result.getData();
                        if(res != null) {
                            String data = res.getStringExtra(AdditemActivity.RES_DATA);
                            entries.add(data);
                            buildAdapter();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildAdapter();
    }

    public void onButtonAddItemClicked(View view)
    {
        Intent intent = new Intent(this, AdditemActivity.class);
        strGetResult.launch(intent);
    }

    private void buildAdapter()
    {
        ListView lv = (ListView) findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, entries);
        lv.setAdapter(adapter);
    }
}