package fr.heban.todoheban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AdditemActivity extends AppCompatActivity {

    public final static String RES_DATA = "fr.heban.todoheban.AddItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
    }

    public void onValidButtonClicked(View view){

        TextView tv = (TextView) findViewById(R.id.input_task_label);
        if(tv.getText().length() == 0) {
            return;
        }
        Intent res = new Intent();
        res.putExtra(RES_DATA, tv.getText().toString());

        this.setResult(RESULT_OK, res);
        this.finish();

    }

    public void onCancelButtonClicked(View view){
        this.setResult(RESULT_CANCELED);
        this.finish();
    }
}