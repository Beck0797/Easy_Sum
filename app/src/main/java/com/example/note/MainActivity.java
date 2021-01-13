package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.Data.DatabaseHandler;
import com.example.note.Model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> noteArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseHandler db;
    private EditText name, amount;
    private TextView txtSum;
    private List<Note> noteList;
    private int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sum = 0;
        name = findViewById(R.id.editTextContent);
        amount = findViewById(R.id.editTextAmount);
        txtSum = findViewById(R.id.sum);
        listView = findViewById(R.id.listview);
        noteArrayList = new ArrayList<>();
        db = new DatabaseHandler(MainActivity.this);

        noteList = db.getAllNotes();
        sum = db.getSum();
        txtSum.setText("Sum: "+sum);

        for(Note n : noteList){
            String s = ""+n.getContent() + " - " + n.getAmount();
            Log.d("main", s);
            noteArrayList.add(s);
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, noteArrayList);
        listView.setAdapter(arrayAdapter);



    }

    public void delete(View view) {
        boolean isSelected = false;
        for(int i = 0; i < listView.getCount(); ++i){
            if(listView.isItemChecked(i)){
                isSelected = true;
                Note note = noteList.get(i);
                // delete from db
                db.delete(note);

                //delete from ui
                noteArrayList.remove(i);
                arrayAdapter.notifyDataSetChanged();
                sum -= note.getAmount();
                txtSum.setText("Sum: "+sum);

                Toast.makeText(this, note.getContent(), Toast.LENGTH_SHORT).show();
            }
        }
        if(!isSelected){
            Toast.makeText(this, "Please, choose an item", Toast.LENGTH_SHORT).show();
        }


    }

    public void ok(View view) {
        if(name.getText().toString().equals("") || amount.getText().toString().equals("")){
            Toast.makeText(this, "Fill all the blanks", Toast.LENGTH_SHORT).show();
        }else{
            int num = 0;
            try {
                num = Integer.parseInt(amount.getText().toString());
            }
            catch (NumberFormatException e)
            {
                num = 0;
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }


            sum += num;
            txtSum.setText("Sum: "+sum);

            String n = name.getText().toString();
            db.addNote(new Note(n, num));

            String s = n + " - " + num;
            noteArrayList.add(s);
            arrayAdapter.notifyDataSetChanged();

            name.setText("");
            amount.setText("");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_scan){
            Intent i = new Intent(this, scan.class);
            startActivity(i);

        }else{
            db.deleteAll();
            noteArrayList.clear();
            arrayAdapter.notifyDataSetChanged();
            sum = 0;
            txtSum.setText("Sum: "+sum);        }

        return super.onOptionsItemSelected(item);
    }

    public void onCopyClicked(View view) {
        if(noteArrayList.isEmpty()){
            Toast.makeText(this, "Please, make an input first!", Toast.LENGTH_SHORT).show();
        }else{
            String text = "";
            for(String s : noteArrayList){
                text +=s;
                text +="\n";
            }
            text += "    Sum: " + sum;
            setClipboard(this, text);

            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }

    }
    public void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public void onEditClicked(View view) {
        for(int i = 0; i < listView.getCount(); ++i){
            if(listView.isItemChecked(i)){
                Note note = noteList.get(i);
                // delete from db
                name.setText(note.getContent());
                amount.setText(""+note.getAmount());

                noteArrayList.remove(i);
                arrayAdapter.notifyDataSetChanged();
                sum -= note.getAmount();
                txtSum.setText("Sum: "+sum);

                db.delete(note);
                noteList.remove(i);




            }
        }
    }
}