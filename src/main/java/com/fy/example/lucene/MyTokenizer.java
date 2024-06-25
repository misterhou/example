package com.fy.example.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;

public class MyTokenizer extends Tokenizer {
    private IKSegmenter ikSegmenter;
    private  final CharTermAttribute charTermAttribute = this.addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAttribute = this.addAttribute(OffsetAttribute.class);

    private final TypeAttribute typeAttribute = this.addAttribute(TypeAttribute.class);
    private int endPosition;

    /**
     * 设置是否使用智能分词，默认false，
     * @param useSmart
     */
    public MyTokenizer(boolean useSmart) {
        super();
        this.ikSegmenter = new IKSegmenter(this.input, useSmart);
    }
    @Override
    public boolean incrementToken() throws IOException {
        this.clearAttributes();
        Lexeme nextLexeme = this.ikSegmenter.next();
        if (nextLexeme != null) {
            this.charTermAttribute.append(nextLexeme.getLexemeText());
            this.charTermAttribute.setLength(nextLexeme.getLength());
            this.offsetAttribute.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            this.endPosition = nextLexeme.getEndPosition();
            this.typeAttribute.setType(nextLexeme.getLexemeTypeString());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        this.ikSegmenter.reset(this.input);
    }

    @Override
    public void end() throws IOException {
        int finalOffset = this.correctOffset(this.endPosition);
        this.offsetAttribute.setOffset(finalOffset, finalOffset);
    }
}
