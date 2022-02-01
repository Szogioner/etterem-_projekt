package hu.progmatic.kozos.etterem.asztal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Getter
@Setter
public class TableViewDto implements Serializable {
  private final Integer id;
  @NotEmpty
  private final String nev;
  @Builder.Default
  private final List<RendelesDto> rendelesDtoList = new ArrayList<>();

  @Builder
  @Data
  @Getter
  @Setter
  public static class RendelesDto implements Serializable {
    private final Integer rendelesId;
    private final String etteremTermekNev;
    private final Integer mennyiseg;
  }
}