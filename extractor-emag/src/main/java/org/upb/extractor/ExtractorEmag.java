package org.upb.extractor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.upb.project.common.extractor.IExtractor;
import org.upb.project.common.jms.JmsProducer;
import org.upb.project.common.model.Product;
import org.upb.project.common.nlp.NameFinderSentences;

public class ExtractorEmag implements IExtractor {

  public void extract(String page, String category) throws Exception {
    Integer sleepTime = (new Random().nextInt(60) + 5);
    System.out.println("SleepTime " + sleepTime);
    Thread.sleep(1000 * sleepTime);
    System.out.println("Stop ");
    Document doc = Jsoup.connect(page).get();
    Elements cards = doc.select(".card");
    for (Element card : cards) {
      String description = card.select(".product-title-zone").text();
      String price = card.select(".product-new-price").text();

      Product annotated = NameFinderSentences.main(description);

      Double dPrice = null;
      try {
        dPrice = Double.valueOf(price.replaceAll("[^0-9\\.]", ""));
      } catch (NumberFormatException e) {

      }
      Product product = Product
          .builder()
          .description(description)
          .price(dPrice)
          .category(category)
          .sites(Arrays.asList("emag"))
          .brand(annotated.getBrand())
          .model(annotated.getModel())
          .date(new Date())
          .build();
      new JmsProducer().run("","","tcp://localhost:61616",toJson(product));
    }
  }

  public List<String> categoryExtracter() {
    return Arrays
        .asList("laptopuri", "telefoane-mobile", "tablete", "smartwatch", "televizoare", "soundbar",
            "casti-audio");
  }

  public List<String> pageExtractor() {
    return Arrays.asList("p1", "p2", "p3", "p4", "p5");
  }

  public void walkAndExtract() {
    String url = "https://www.emag.ro/{0}/vendor/emag/{1}/c";
    categoryExtracter().forEach(category -> {
      pageExtractor().forEach(page -> {
        try {
          extract(MessageFormat.format(url, new Object[]{category, page}), category);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });
  }

}
