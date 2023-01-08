package org.upb.project.presentation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.upb.project.common.model.Product;
import org.upb.project.common.nlp.NameFinderSentences;
import org.upb.project.model.Body;
import org.upb.project.repository.ProductRepository;
import org.upb.project.services.Convertor;
import org.upb.project.services.GenericCache;

@RestController
@RequestMapping(path = "/api")
@Slf4j
public class PublicController implements Convertor {

  private GenericCache<String, List<Product>> conversionCache = new GenericCache<>();

  @Autowired
  private ProductRepository repository;

  @GetMapping("/search")
  public ResponseEntity search(@RequestParam(value = "product") String product,
      @RequestParam(value = "brand") String brand, @RequestParam(value = "model") String model) {
    if (Strings.isNotEmpty(product)) {
      return ResponseEntity.ok(repository.findByDescription(product));
    }
    if (Strings.isNotEmpty(brand) && Strings.isNotEmpty(model)) {
      return ResponseEntity.ok(repository.findByBrandAndModel(brand, model));
    }
    if (Strings.isNotEmpty(model)) {
      return ResponseEntity.ok(repository.findByModel(model));
    }
    return ResponseEntity.ok(null);
  }

  @GetMapping("/predict")
  public ResponseEntity productCode(@RequestParam(value = "code") String code) {
    return ResponseEntity.ok(null);
  }

  @PostMapping("/server")
  public ResponseEntity search(@RequestBody Body body) throws Exception {
    List<Product> products = new ArrayList<>();
    for (String s : body.getHtml().split("\n")) {
      if (!Strings.isBlank(s)) {
        s = s.trim();
        List<Product> p;
        if ((p = conversionCache.get(s)) == null) {
          Product toSearch = NameFinderSentences.main(s);
          toSearch.setDescription(s);
          log.info("{}", toSearch.toString());
          if (toSearch.getBrand() != null && toSearch.getModel() != null) {
            p = toWeb(repository.findByBrandAndModel(toSearch.getBrand(), toSearch.getModel()));
            conversionCache.put(s, p);
          } else {
            p = null;
          }
        }
        if (p != null) {
          products.addAll(p);
        }

      }
    }
    if (products.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    StringBuilder builder = new StringBuilder();

    for (Product p : products) {
      builder = builder.append(p.getBrand()).append(";")
          .append(p.getModel() == null ? "" : p.getModel()).append(";")
          .append(p.getCategory() == null ? "" : p.getCategory()).append(";")
          .append(p.getSites() == null ? "" : p.getSites()).append(";")
          .append(p.getPrice() == null ? "" : p.getPrice()).append(";")
          .append(p.getDate() == null ? "" : p.getDate())
          .append("\n");
    }
    List<Product> sortedList = products.stream()
        .sorted(Comparator.comparingDouble(Product::getPrice))
        .collect(Collectors.toList());

    if (body.getSite().contains(sortedList.get(0).getSites().get(0))) {
      builder = builder.append("Cumpara");
    } else {
      builder = builder.append("Nu cumpara");
    }
    return ResponseEntity.ok(builder.toString());
  }

}
