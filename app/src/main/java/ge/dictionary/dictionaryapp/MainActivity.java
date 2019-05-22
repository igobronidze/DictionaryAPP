package ge.dictionary.dictionaryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import ge.dictionary.dictionaryapp.processor.WordsProcessor;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String filePath = "dictionary.txt";

    private WordsProcessor wordsProcessor;

    private Word word;

    private WordViewMode mode = WordViewMode.WORD;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        setContentView(R.layout.activity_main);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filePath);

        wordsProcessor = new WordsProcessor(file);
        word  = wordsProcessor.getRandomWord();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateView(word, mode);

        LinearLayout linearLayout = findViewById(R.id.wordLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == WordViewMode.WORD_EXPLAIN_TRANSLATE) {
                    wordsProcessor.increaseWordTotalShows(word);
                    word = wordsProcessor.getRandomWord();
                }

                mode = getNextMode(mode);

                updateView(word, mode);
            }
        });

        Button yesButton = findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsProcessor.increaseWordRankAndTotalShows(word);

                mode = WordViewMode.WORD;
                word = wordsProcessor.getRandomWord();

                updateView(word, mode);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateView(Word word, WordViewMode mode) {
        TextView wordTextView = findViewById(R.id.wordTextView);
        TextView explainTextView = findViewById(R.id.explainTextView);
        TextView translateTextView = findViewById(R.id.translateTextView);

        wordTextView.setText(word.getWord());
        explainTextView.setText(word.getExplain());
        translateTextView.setText(word.getTranslate());

        switch (mode) {
            case WORD:
                wordTextView.setVisibility(View.VISIBLE);
                explainTextView.setVisibility(View.INVISIBLE);
                translateTextView.setVisibility(View.INVISIBLE);
                break;
            case WORD_EXPLAIN:
                wordTextView.setVisibility(View.VISIBLE);
                explainTextView.setVisibility(View.VISIBLE);
                translateTextView.setVisibility(View.INVISIBLE);
                break;
            case WORD_EXPLAIN_TRANSLATE:
                wordTextView.setVisibility(View.VISIBLE);
                explainTextView.setVisibility(View.VISIBLE);
                translateTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private WordViewMode getNextMode(WordViewMode mode) {
        switch (mode) {
            case WORD:
                return WordViewMode.WORD_EXPLAIN;
            case WORD_EXPLAIN:
                return WordViewMode.WORD_EXPLAIN_TRANSLATE;
            case WORD_EXPLAIN_TRANSLATE:
            default:
                return WordViewMode.WORD;
        }
    }
}
