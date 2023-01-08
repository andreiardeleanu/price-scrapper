package org.upb.project.common.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Product {

  private String id;
  private Double price;
  private String description;
  private Date date;
  private String category;
  private String brand;
  private List<String> sites;
  private String model;

  @Override
  public String toString() {
    return "Product{" +
        "id='" + id + '\'' +
        ", price=" + price +
        ", model=" + model +
        ", description='" + description + '\'' +
        ", date=" + date +
        ", category='" + category + '\'' +
        ", brand='" + brand + '\'' +
        ", sites=" + sites +
        '}';
  }
}
