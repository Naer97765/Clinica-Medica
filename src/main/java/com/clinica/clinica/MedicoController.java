
package com.clinica.clinica;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MedicoController {

    
    private final ClinicaService clinicaService;

    public MedicoController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @GetMapping("/medicos")
    public String listarMedicos(Model model) {

        model.addAttribute("medicos", clinicaService.listarMedicos());

        return "medicos";
    }

    @PostMapping("/medicos")
    public String cadastrarMedico(
            @RequestParam String nome,
            @RequestParam String especialidade) {

        clinicaService.cadastrarMedico(nome, especialidade);

        return "redirect:/medicos";
    }
}
