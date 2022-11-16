package fr.heban.todoheban;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> entries = new ArrayList<String>(Arrays.asList("item1","item2","item3","item4","item5","item6","item7","item8"));
    ArrayAdapter<String> adapter = null;

    /*
        Retour intention explicite, retourne un string qui représente l'activité à ajouter
     */
    private final ActivityResultLauncher<Intent> strGetResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Si le resultat est OK
                    if (result.getResultCode() == RESULT_OK) {
                        Intent res = result.getData();
                        //Si il y a bien une donnée
                        if (res != null) {
                            String data = res.getStringExtra(AdditemActivity.RES_DATA);
                            //Ajout dans les entries
                            entries.add(data);
                            //On rebuild l'adaptateur
                            buildAdapter();
                        }
                    }
                }
            }
    );


    /**
     * À la création de l'activité
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadItems();
    }

    /**
     * Création du menu option
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    /**
     * On catch l'événement quand une option du menu option est cliquée
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //Si le bouton de suppression est sélectionner
        if (id == R.id.menu_item_delete_checked) {
            this.deleteCheckedItem();
        } else if (id == R.id.menu_item_save_data) {
            //bouton pour sauvegarder
            this.saveItems();
        }
        return true;
    }

    /**
     * Événement appelée lors du clique sur le bouton pour afficher l'activité pour créer un item dans la liste
     * @param view
     */
    public void onButtonAddItemClicked(View view) {
        //On démarre une nouvelle activité
        Intent intent = new Intent(this, AdditemActivity.class);
        strGetResult.launch(intent);
    }

    /**
     * Méthode permettant de créer/mettre à jour l'adaptateur. Et ansi mettre à jour les données.
     */
    private void buildAdapter() {
        //On récupère a liste view
        ListView lv = (ListView) findViewById(R.id.listview);
        //On recréer une nouvelle instance d'un array adapter
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, this.entries);
        //On ajoute l'array adapter dans le list view
        lv.setAdapter(this.adapter);
    }

    /**
     * Suppression des items sélectionnées dans la liste
     */
    private void deleteCheckedItem() {
        ListView lv = (ListView) findViewById(R.id.listview);
        //Le SparseBooleanArray permet de renvoyer un array avec les items qui sont sélectionnés
        SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
        ArrayList<String> checkedValues = new ArrayList<>();

        //On itère dans le sparseArray
        for (int i = 0; i < sparseBooleanArray.size(); ++i) {
            //Si il est sélectionner on l'ajoute dans le tableau des éléments à supprimer
            //Aucun problème lors de la suppression car tout fonctionne avec des références !
            if (sparseBooleanArray.valueAt(i) && this.adapter != null) {
                checkedValues.add(entries.get(sparseBooleanArray.keyAt(i)));
            }
        }
        //On supprime les éléments sélectionnés
        for(int i=0;i<checkedValues.size();++i) {
            this.entries.remove(checkedValues.get(i));
        }
        //On met à jour les items de la liste
        this.buildAdapter();
        //On remet à zéro la sélection
        lv.clearChoices();
    }

    /**
     * Sauvegarde des données dans un fichier nommé data.txt
     */
    private void saveItems()
    {
        String content = "";
        for (String item : this.entries) {
            content += item + "\n";
        }

        try {
            //le fichier est située à /data/data/fr.heban.todoheban/files/data.txt
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("data.txt", Context.MODE_PRIVATE));
            //on écrit dans le fichier
            outputStreamWriter.write(content);
            //on ferme le flux d'écriture
            outputStreamWriter.close();
        } catch (IOException e) {
            //Affichage d'une erreur
            Toast.makeText(getBaseContext(), R.string.app_error_when_save, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Chargement des données depuis un fichier nommé data.txt
     */
    private void loadItems()
    {
        try {
            //le fichier est située à /data/data/fr.heban.todoheban/files/data.txt
            InputStreamReader inputStreamReader = new InputStreamReader(getBaseContext().openFileInput("data.txt"));

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            // Tout le temps que l'on peut lire une ligne on la lit
            while ((receiveString = bufferedReader.readLine()) != null) {
                //Si notre string builder est vide on ajoute la ligne
                if(stringBuilder.toString().equals("")) {
                    stringBuilder.append(receiveString);
                } else {
                    //s'il n'est pas vide, on ajoute d'abord un saut de ligne puis notre ligne
                    //permettra de faire un split plus tard
                    stringBuilder.append('\n').append(receiveString);
                }
            }
            //on ferme de flux
            inputStreamReader.close();
            //Convertit notre stringBuilder en string
            String content = stringBuilder.toString();
            //On le split à chaque saut de ligne
            String[] splittedData = content.split("\n");
            ArrayList<String> data = new ArrayList<String>();

            //On ajoute dans le tableau
            for (String item : splittedData) {
                data.add(item);
            }

            //On met à jour les données
            this.entries = data;

            this.buildAdapter();

        } catch (IOException ignored) {
            this.buildAdapter();
        }

    }
}