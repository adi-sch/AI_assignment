package com.ai.aiassignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity
{
    private EditText eTLanguage, eTWords;
    private TextView tVResult;
    private GeminiManager geminiManager;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        eTLanguage = findViewById(R.id.eTLanguage);
        eTWords = findViewById(R.id.eTWords);
        tVResult = findViewById(R.id.tVResult);
        tVResult.setMovementMethod(new ScrollingMovementMethod());

        geminiManager = GeminiManager.getInstance();
    }

    public void textPrompt(View view)
    {
        String language = eTLanguage.getText().toString();
        String words = eTWords.getText().toString();
        if (language.isEmpty() || words.isEmpty())
        {
            tVResult.setText("Please enter both language and words.");
        }
        else
        {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            String prompt = "Create a story in " + language + " with " + words +
                    " words. return only the name of the story and the story.";
            ProgressDialog pD = new ProgressDialog(this);
            pD.setTitle("Sent Prompt");
            pD.setMessage("Waiting for response...");
            pD.setCancelable(false);
            pD.show();
            geminiManager.sendTextPrompt(prompt,
                    new GeminiCallback()
                    {
                        @Override
                        public void onSuccess(String result) {
                            pD.dismiss();
                            tVResult.setText(result);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            pD.dismiss();
                            tVResult.setText("Failed prompting Gemini");
                            Log.e(TAG, "textPrompt/ Error: " + error.getMessage());
                        }
                    });
        }
    }
}