package org.upb.project.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.upb.project.entities.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

  List<Product> findByDescription(String description);
  List<Product> findByBrandAndModel(String brand,String model);
  List<Product> findByModel(String model);
}
