package org.upb.project.entities;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class Product {

  @Id
  private String id;
  private Double price;
  private String description;
  @CreatedDate
  private Date date;
  private String category;
  private String brand;
  private String model;
  private List<String> sites;

}
