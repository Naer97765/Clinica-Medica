
package com.clinica.clinica;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MedicoController {

    private final List<Medico> medicos = new ArrayList<>();

    @GetMapping("/medicos")
    public String listarMedicos(Model model) {

        model.addAttribute("medicos", medicos);

        return "medicos";
    }

    @PostMapping("/medicos")
    public String cadastrarMedico(
            @RequestParam String nome,
            @RequestParam String crm,
            @RequestParam String especialidade) {

        Medico medico = new Medico(nome, crm, especialidade);

        medicos.add(medico);

        return "redirect:/medicos";
    }
}
