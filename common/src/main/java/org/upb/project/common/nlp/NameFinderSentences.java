package org.upb.project.common.nlp;

import com.google.common.io.Resources;
import java.io.InputStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.upb.project.common.model.Product;

public class NameFinderSentences {

  public static Product main(String sentence) throws Exception {
    Product product = Product.builder().build();
    //Loading the tokenizer model
    InputStream inputStreamTokenizer = Resources.getResource("en-token.bin").openStream();
    TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer);

    //Instantiating the TokenizerME class
    TokenizerME tokenizer = new TokenizerME(tokenModel);

    //Tokenizing the sentence in to a string array
    String tokens[] = tokenizer.tokenize(sentence);

    //Loading the NER-person model
    InputStream inputStreamNameFinder = Resources.getResource("ner-custom-model.bin").openStream();
    TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);

    //Instantiating the NameFinderME class
    NameFinderME nameFinder = new NameFinderME(model);

    //Finding the names in the sentence
    Span nameSpans[] = nameFinder.find(tokens);

    //Printing the names and their spans in a sentence
    for (Span s : nameSpans) {
      if (s.getType().equals("organization")) {
        product.setBrand(tokens[s.getStart()]);
      }
      if (s.getType().equals("model")) {
        product.setModel(tokens[s.getStart()]);
      }
    }
    return product;
  }
}