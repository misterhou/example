package com.fy.example.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

public class MyIKAnalyzer extends Analyzer {
    private boolean useSmart;

    public MyIKAnalyzer() {
        this(false);
    }

    public MyIKAnalyzer(boolean useSmart) {
        this.useSmart = useSmart;
    }
    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer tokenizer = new MyTokenizer(this.useSmart());
        return new TokenStreamComponents(tokenizer);
    }

    public boolean useSmart() {
        return this.useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }
}
