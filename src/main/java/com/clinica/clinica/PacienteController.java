
package com.clinica.clinica;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PacienteController {

    private final List<Paciente> pacientes = new ArrayList<>();

    // Exibir a página de pacientes
    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
    model.addAttribute("pacientes", pacientes);
    return "pacientes";
}
    

    // Cadastrar um novo paciente
    @PostMapping("/pacientes")
    public String cadastrarPaciente(
            @RequestParam String nome,
            @RequestParam String documento) {

        Paciente novoPaciente = new Paciente(
            nome,
            documento
        );

        pacientes.add(novoPaciente);

        return "redirect:/pacientes";
    }
}
