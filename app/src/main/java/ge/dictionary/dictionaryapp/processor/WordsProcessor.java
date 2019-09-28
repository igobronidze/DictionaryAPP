package ge.dictionary.dictionaryapp.processor;

import ge.dictionary.dictionaryapp.TestDataCreator;
import ge.dictionary.dictionaryapp.Word;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class WordsProcessor {

    private List<Word> words;

    private File file;

    private boolean randomMode = true;

    private int index = 0;

    public WordsProcessor(File file) {
        this.file = file;
        if (file != null) {
            words = WordsDataProcessor.read(file);
        } else {
            words = TestDataCreator.getData();
        }
        sortWords();
    }

    public void setRandomMode(boolean randomMode) {
        this.randomMode = randomMode;
        index = 0;
    }

    public void increaseWordRankAndTotalShows(Word word) {
        word.setRank(word.getRank() + 1);
        word.setTotalShows(word.getTotalShows() + 1);
        if (file != null) {
            WordsDataProcessor.save(words, file);
        }
    }

    public void increaseWordTotalShows(Word word) {
        word.setTotalShows(word.getTotalShows() + 1);
        if (file != null) {
            WordsDataProcessor.save(words, file);
        }
    }

    public Word getWord() {
        if (words.isEmpty()) {
            return null;
        }
        if (words.size() == 1) {
            return words.get(0);
        }

        if (!randomMode) {
            Word word = words.get(index);
            index++;
            if (index > words.size()) {
                index = 0;
            }

            return word;
        }

        List<Integer> coefficients = new ArrayList<>();

        int max = 0;
        for (Word word : words) {
            max = Math.max(max, word.getRank());
        }

        for (Word word : words) {
            coefficients.add(max + 1 - word.getRank());
        }

        int sum = 0;
        for (int i = 0; i < coefficients.size(); i++) {
            sum += coefficients.get(i);
            coefficients.set(i, sum);
        }

        Random random = new Random();
        int randomNumber = random.nextInt(sum);

        if (randomNumber < coefficients.get(1)) {
            return words.get(0);
        }
        for (int i = 1; i < words.size(); i++) {
            if (coefficients.get(i) > randomNumber) {
                return words.get(i);
            }
        }

        return null;
    }

    private void sortWords() {
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                if (w1.getRank() == w2.getRank()) {
                    return new Random().nextInt(3) - 1;
                }
                return w1.getRank() - w2.getRank();
            }
        });
    }
}
