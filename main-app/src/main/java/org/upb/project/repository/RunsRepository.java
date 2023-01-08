package org.upb.project.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.upb.project.entities.Runs;

@Repository
public interface RunsRepository extends MongoRepository<Runs, String> {

  List<Runs> findByRunDateAndExtractor(Date runDate, String extractor);
}
