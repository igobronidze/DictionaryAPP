package ge.dictionary.dictionaryapp.processor;

import ge.dictionary.dictionaryapp.TestDataCreator;
import ge.dictionary.dictionaryapp.Word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordsProcessor {

    private List<Word> words;

    private File file;

    public WordsProcessor(File file) {
        this.file = file;
        if (file != null) {
            words = WordsDataProcessor.read(file);
        } else {
            words = TestDataCreator.getData();
        }
    }

    public void increaseWordRank(Word word) {
        word.setRank(word.getRank() + 1);
        WordsDataProcessor.save(words, file);
    }

    public Word getRandomWord() {
        if (words.isEmpty()) {
            return null;
        }
        if (words.size() == 1) {
            return words.get(0);
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
}
