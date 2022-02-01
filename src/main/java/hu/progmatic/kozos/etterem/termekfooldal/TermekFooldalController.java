package hu.progmatic.kozos.etterem.termekfooldal;


import hu.progmatic.kozos.etterem.asztal.AsztalService;
import hu.progmatic.kozos.etterem.asztal.TableViewDto;
import hu.progmatic.kozos.etterem.leltar.EtteremTermek;
import hu.progmatic.kozos.etterem.leltar.EtteremTermekDto;
import hu.progmatic.kozos.etterem.leltar.EtteremTermekService;
import hu.progmatic.kozos.etterem.leltar.Tipus;
import hu.progmatic.kozos.etterem.rendeles.CreateRendelesCommand;
import hu.progmatic.kozos.etterem.rendeles.Rendeles;
import hu.progmatic.kozos.etterem.rendeles.RendelesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TermekFooldalController {
  @Autowired
  private AsztalService asztalService;
  @Autowired
  private RendelesService rendelesService;
  @Autowired
  private EtteremTermekService etteremTermekService;

  @GetMapping("/etterem/asztal/{asztalId}/{tipus}")
  public String itemMain(
      @PathVariable Integer asztalId,
      @PathVariable AsztalFeluletTipus tipus,
      Model model) {
    TableViewDto dto = asztalService.getTableViewDto(asztalId);
    model.addAttribute("tableViewDto", dto);
    getGombDtoList(asztalId, tipus, model);
    return "etterem/termek_fooldal";
  }

  @GetMapping("/etterem/asztal/{asztalId}/tipus/{tipus}")
  public String dishes(
      @PathVariable Integer asztalId,
      @PathVariable Tipus tipus,
      Model model
  ) {
    model.addAttribute(
        "filteredByTipus",
        etteremTermekService.findAllByTipus(tipus)
    );
    model.addAttribute("tableViewDto", asztalService.getTableViewDto(asztalId));
    return "etterem/termek_fooldal";
  }

  @PostMapping("/etterem/asztal/{asztalId}/tipus/{tipus}")
  public String createOrder(
      @PathVariable Integer asztalId,
      @PathVariable Tipus tipus,
      @ModelAttribute("createRendelesCommand") @Valid CreateRendelesCommand command,
      BindingResult bindingResult,
      Model model) {
    if (!bindingResult.hasErrors()) {
      asztalId = asztalService.getIdByAsztalSzam(asztalId);
      command.setAsztalId(asztalId);
      rendelesService.create(command);
      model.addAttribute("tableViewDto", asztalService.getTableViewDto(asztalId));
      refreshAllItem(model);
      clearFormItem(model);
    }
    model.addAttribute(
        "filteredByTipus",
        etteremTermekService.findAllByTipus(tipus)
    );
    return "etterem/termek_fooldal";
  }

  private void getGombDtoList(Integer asztalId, AsztalFeluletTipus tipus, Model model) {
    List<GombDto> gombok = new ArrayList<>();
    if (tipus == AsztalFeluletTipus.ETEL) {
      gombok = List.of(
          new GombDto("LEVES", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.LEVES.name()),
          new GombDto("ELŐÉTEL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.ELOETEL.name()),
          new GombDto("SERTÉSÉTEL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.SERTESETEL.name()),
          new GombDto("HALÉTEL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.HALETEL.name()),
          new GombDto("MARHA", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.MARHAETEL.name()),
          new GombDto("DESSZERT", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.DESSZERT.name())
      );
    } else if (tipus == AsztalFeluletTipus.ITAL) {
      gombok = List.of(
          new GombDto("ALKOHOL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.ALKOHOL.name()),
          new GombDto("RÖVIDITAL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.ROVIDITAL.name()),
          new GombDto("KOKTÉL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.KOKTEL.name()),
          new GombDto("ÜDÍTŐ", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.UDITO.name()),
          new GombDto("FORRÓ ITAL", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.FORROITAL.name()),
          new GombDto("KÁVÉ", "/etterem/asztal/" + asztalId + "/tipus/" + Tipus.KAVE.name())
      );
    } else {
      gombok = List.of(
          new GombDto("ÉTEL", "/etterem/asztal/" + asztalId + "/" + AsztalFeluletTipus.ETEL.name()),
          new GombDto("ITAL", "/etterem/asztal/" + asztalId + "/" + AsztalFeluletTipus.ITAL.name()),
          new GombDto("SZÁMLA", "/etterem/asztal/" + asztalId + "/szamla")
      );
    }
    model.addAttribute(
        "gombDtoList",
        gombok
    );
  }

  @ModelAttribute("tableViewDto")
  public TableViewDto tableViewDto() {
    return TableViewDto.builder().build();
  }

  @ModelAttribute("gombDtoList")
  public List<GombDto> gombDtoList() {
    return List.of();
  }

  @ModelAttribute("rendelesItems")
  List<EtteremTermekDto> rendelesItems() {
    return List.of();
  }

  @ModelAttribute("formItem")
  public Rendeles formItem() {
    return new Rendeles();
  }

  @ModelAttribute("filteredByTipus")
  public List<EtteremTermek> filteredByTipus() {
    return List.of();
  }

  private void clearFormItem(Model model) {
    model.addAttribute("formItem", formItem());
  }

  private void refreshAllItem(Model model) {
    model.addAttribute("rendelesItems", rendelesItems());
  }
}