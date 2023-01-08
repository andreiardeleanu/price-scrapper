package org.upb.project.common.extractor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.upb.project.common.model.Product;

public interface IExtractor {

  ObjectMapper mapper = new ObjectMapper();
  void walkAndExtract();

  @SneakyThrows
  default String toJson(Product product) {
  return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(product);
  }
}
