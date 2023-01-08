package org.upb.project.entities;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
public class Runs {
  @Id
  private String id;
  private Date runDate;
  private String extractor;
}
