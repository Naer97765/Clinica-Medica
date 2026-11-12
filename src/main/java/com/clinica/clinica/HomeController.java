package com.clinica.clinica;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ClinicaService clinicaService;

    public HomeController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("medicos", clinicaService.listarMedicos());
        model.addAttribute("consultas", clinicaService.listarConsultas());
        return "index";
    }
}
