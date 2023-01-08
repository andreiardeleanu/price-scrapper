package org.upb.project.services;

import java.util.ArrayList;
import java.util.List;
import org.upb.project.common.model.Product;

public interface Convertor {

  default List<Product> toWeb(List<org.upb.project.entities.Product> products) {
    List<Product> result = new ArrayList<>();
    for (org.upb.project.entities.Product p : products) {
      Product product = Product.builder()
          .brand(p.getBrand())
          .category(p.getCategory())
          .description(p.getDescription())
          .price(p.getPrice())
          .model(p.getModel())
          .sites(p.getSites())
          .build();
      result.add(product);
    }
    return result;
  }
}
