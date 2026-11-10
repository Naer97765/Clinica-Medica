
package com.clinica.clinica;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConsultaController {

    private final ClinicaService clinicaService;

    public ConsultaController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    

    @GetMapping("/consultas")
public String listarConsultas(Model model) {

    System.out.println("Médicos encontrados: "
        + clinicaService.listarMedicos().size());

    System.out.println("Pacientes encontrados: "
        + clinicaService.listarPacientes().size());

    model.addAttribute("medicos",
        clinicaService.listarMedicos());

    model.addAttribute("pacientes",
        clinicaService.listarPacientes());

    model.addAttribute("consultas",
        clinicaService.listarConsultas());

    return "consultas";
}
    @PostMapping("/consultas")
public String agendarConsulta(
        @RequestParam("crm") String crm,
        @RequestParam("documento") String documento,
        @RequestParam("dataHora") String dataHora,
        RedirectAttributes redirect) {

    try {
        clinicaService.agendarConsulta(crm, documento, dataHora);

        redirect.addFlashAttribute(
                "sucesso", "Consulta agendada com sucesso!");

    } catch (java.time.format.DateTimeParseException erro) {
        redirect.addFlashAttribute(
                "erro", "Informe uma data e um horário válidos.");

    } catch (IllegalArgumentException erro) {
        redirect.addFlashAttribute(
                "erro", erro.getMessage());
    }

    return "redirect:/consultas";
}
    @PostMapping("/consultas/{indice}/cancelar")
    public String cancelarConsulta(
            @PathVariable int indice,
            RedirectAttributes redirect) {

        try {
            clinicaService.cancelarConsulta(indice);

            redirect.addFlashAttribute(
                "sucesso", "Consulta cancelada!");

        } catch (IllegalArgumentException erro) {
            redirect.addFlashAttribute(
                "erro", erro.getMessage());
        }

        return "redirect:/consultas";    
    }
}
