package org.upb.project.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.upb.project.entities.Runs;
import org.upb.project.repository.RunsRepository;

@Service
@Slf4j
public class LoadAndRun {


  private final Map<String, String> transformers = new HashMap<>();

  @Autowired
  private RunsRepository repository;

  @PostConstruct
  public void init() {
    transformers.put("emag", "org.upb.extractor.ExtractorEmag");
    transformers.put("flanco", "org.upb.extractor.ExtractorFlanco");
    transformers.put("altex", "org.upb.extractor.ExtractorAltex");
  }

  @Scheduled(fixedDelay = 10000)
  public void run() throws IOException {
    File dir = new File("/var/tmp/extractors");
    Arrays.stream(dir.listFiles()).parallel().forEach( transformer -> {
      transformers.entrySet().parallelStream().forEach(entry -> {
        try {
          if (transformer.getName().contains(entry.getKey())) {
            if (repository.findByRunDateAndExtractor(new Date(), transformer.getName()).isEmpty()) {
              run(entry.getValue(), new URL("file:".concat(transformer.getAbsolutePath())));
              Runs runs = Runs.builder().runDate(new Date())
                  .extractor(transformer.getName()).build();
              repository.insert(runs);
            }
          }
        } catch (MalformedURLException e) {
          log.error("Exception {}", e);
        }
      });
    }
    );
  }

  private void run(String classForName, URL jarUrl) {
    try {
      ClassLoader loader = URLClassLoader.newInstance(
          new URL[]{jarUrl},
          getClass().getClassLoader()
      );
      Class classToLoad = Class.forName(
          classForName, true,
          loader);
      classToLoad.newInstance();
      Method method = classToLoad.getDeclaredMethod("walkAndExtract");
      Object instance = classToLoad.newInstance();
      method.invoke(instance);
    } catch (Exception e) {
      throw new RuntimeException("Unexpected exception", e);
    }
  }

}
