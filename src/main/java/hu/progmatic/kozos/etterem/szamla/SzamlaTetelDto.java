package hu.progmatic.kozos.etterem.szamla;

import hu.progmatic.kozos.etterem.rendeles.RendelesDto;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SzamlaTetelDto implements Serializable {
  private Integer id;
  private RendelesDto rendelesDto;
  private Integer nemFizetettMennyiseg;
  @Builder.Default
  private Integer fizetettMennyiseg = 0;
}