package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class scan extends AppCompatActivity {
    private Button btnPaste, btnCopy;
    private TextView txt;
    private EditText edTxtOutput;
    private String result, input;
    private int sum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        btnCopy = findViewById(R.id.btnCopy);
        btnPaste = findViewById(R.id.btnPaste);
        txt = findViewById(R.id.txtOutput);
        edTxtOutput = findViewById(R.id.edTxtInput);
        result = "";
        input = "";
        sum = 0;





        btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edTxtOutput.getText().toString().equals("")){
                    Toast.makeText(scan.this, "No input to scan", Toast.LENGTH_SHORT).show();
                }else{
                    input = "";
                    sum = 0;
                    txt.setText("");
                    getNums(edTxtOutput.getText().toString());
                    txt.setText(input);
                }

            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.equals("")){
                    Toast.makeText(scan.this, "Please, make an input first!", Toast.LENGTH_SHORT).show();
                }else{
                    String s = edTxtOutput.getText().toString();
                    s += "\n   = " + sum;
                    setClipboard(scan.this, s);
                    Toast.makeText(scan.this, "Result is copied", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void getNums(String s){
        String str = "";
        for(int i = 0; i < s.length(); ++i){
            str += s.charAt(i);
            if(s.charAt(i) == ' ' || s.charAt(i) =='\n'|| i == s.length()-1){
                if(Character.isDigit(str.charAt(0))){

                    if(str.charAt(str.length()-1) == '\n' || str.charAt(str.length()-1) == ' '){
                        input += str.substring(0, str.length()-1)+ " ";
                        strToInt(str.substring(0, str.length()-1));
                    }else{
                        input += str + " ";
                        strToInt(str);
                    }
                    if(i == s.length() - 1){
                        input += "= " + sum;
                    }else{
                        input += "+ ";
                    }




                }
                str ="";
            }


        }
        Log.d("str", input);

    }
    void strToInt(String str){
        int foo = 0;
        try {
            foo = Integer.parseInt(str);
            sum += foo;
        }
        catch (NumberFormatException e)
        {
            foo = 0;
            sum = 0;
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
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
}