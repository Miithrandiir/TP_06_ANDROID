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

    /**
     * Bouton de validation d'ajout d'une tâche
     * @param view
     */
    public void onValidButtonClicked(View view){

        TextView tv = (TextView) findViewById(R.id.input_task_label);
        if(tv.getText().length() == 0) {
            return;
        }
        Intent res = new Intent();
        //On ajoute en retour la valeur du champs
        res.putExtra(RES_DATA, tv.getText().toString());

        this.setResult(RESULT_OK, res);
        this.finish();

    }

    /**
     * Bouton d'annulation d'ajouter d'une tâche
     * @param view
     */
    public void onCancelButtonClicked(View view){
        this.setResult(RESULT_CANCELED);
        this.finish();
    }
}