package fr.heban.todoheban;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> entries = new ArrayList<String>(Arrays.asList("item1","item2","item3","item4","item5","item6","item7","item8"));
    ArrayAdapter<String> adapter = null;

    private final ActivityResultLauncher<Intent> strGetResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent res = result.getData();
                        if (res != null) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_delete_checked) {
            this.deleteCheckedItem();
        }
        return true;
    }

    public void onButtonAddItemClicked(View view) {
        Intent intent = new Intent(this, AdditemActivity.class);
        strGetResult.launch(intent);
    }

    private void buildAdapter() {
        ListView lv = (ListView) findViewById(R.id.listview);

        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, entries);
        lv.setAdapter(this.adapter);
    }

    private void deleteCheckedItem() {
        ListView lv = (ListView) findViewById(R.id.listview);

        SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
        ArrayList<String> checkedValues = new ArrayList<>();

        for (int i = 0; i < sparseBooleanArray.size(); ++i) {
            if (sparseBooleanArray.valueAt(i) && this.adapter != null) {
                checkedValues.add(entries.get(sparseBooleanArray.keyAt(i)));
            }
        }

        for(int i=0;i<checkedValues.size();++i) {
            this.entries.remove(checkedValues.get(i));
        }

        this.buildAdapter();

        lv.clearChoices();
    }
}