package ge.dictionary.dictionaryapp;

public class Word {

    private String word;

    private String explain;

    private String translate;

    private int rank;

    public Word() {}

    public Word(String word, String explain, String translate) {
        this.word = word;
        this.explain = explain;
        this.translate = translate;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setWord(Word word) {
        this.word = word.getWord();
        this.explain = word.getExplain();
        this.translate = word.getTranslate();
        this.rank = word.getRank();
    }
}
