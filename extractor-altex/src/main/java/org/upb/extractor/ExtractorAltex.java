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

public class ExtractorAltex implements IExtractor {

  public void extract(String page,String category) throws Exception {
    Integer sleepTime = (new Random().nextInt(60) + 5);
    System.out.println("SleepTime " + sleepTime);
    Thread.sleep(1000 * sleepTime);
    System.out.println("Stop ");
    Document doc = Jsoup.connect(page).get();
    Elements cards = doc.select(".Product");
    for (Element card : cards) {
      String description = card.select(".Product-name ").text();
      String price = card.select(".Price-current").select(".Price-int").text().replaceAll("\\.","")
          + "," +  card.select(".Price-current").select("Price-dec").text();

      Product annotated = NameFinderSentences.main(description);

      Product product = Product
          .builder()
          .description(description)
          .price(Double.valueOf(price.replaceAll("[^0-9\\.]", "")))
          .category(category)
          .sites(Arrays.asList("altex"))
          .brand(annotated.getBrand())
          .model(annotated.getModel())
          .date(new Date())
          .build();
      new JmsProducer().run("","","tcp://localhost:61616",toJson(product));
    }
  }

  public List<String> categoryExtracter() {
    return Arrays
        .asList("laptopuri", "telefoane", "tablete", "smartwatches", "televizoare", "soundbar-home-cinema",
            "casti-bluetooth-on-ear-telefon");
  }

  public List<String> pageExtractor() {
    return Arrays.asList("1", "2", "3", "4", "5");
  }

  public void walkAndExtract() {
    String url = "https://altex.ro/{0}/cpl/filtru/p/{1}/";
    categoryExtracter().forEach(category -> {
      pageExtractor().forEach(page -> {
        try {
          extract(MessageFormat.format(url, new Object[]{category, page}),category);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    });
  }

}
