package org.upb.project.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.upb.project.entities.Product;
import org.upb.project.repository.ProductRepository;

@Component
@Slf4j
public class JmsReceiver {

  @Autowired
  private ProductRepository repository;

  @JmsListener(destination = "input")
  public void receiveMessage(String message) throws JsonProcessingException {
    Product product = new ObjectMapper().readValue(message, Product.class);
    log.info("Received info about product {}.", product.getDescription());
    repository.save(product);
  }

}
