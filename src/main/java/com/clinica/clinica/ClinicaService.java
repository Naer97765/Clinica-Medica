
package com.clinica.clinica;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClinicaService {

    private final List<Medico> medicos = new ArrayList<>();
    private final List<Paciente> pacientes = new ArrayList<>();
    private final List<Consulta> consultas = new ArrayList<>();

    // MÉDICOS

    public synchronized void cadastrarMedico(
            String nome, String crm, String especialidade) {

        medicos.add(new Medico(nome, crm, especialidade));
    }

    public synchronized List<Medico> listarMedicos() {
        return List.copyOf(medicos);
    }

    // PACIENTES

    public synchronized void cadastrarPaciente(
            String nome, String documento) {

        pacientes.add(new Paciente(nome, documento));
    }

    public synchronized List<Paciente> listarPacientes() {
        return List.copyOf(pacientes);
    }

    // CONSULTAS

    public synchronized void agendarConsulta(
            String crm, String documento, String dataHora) {

        Medico medico = medicos.stream()
                .filter(m -> m.getCrm().equals(crm))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Médico não cadastrado!"));

        Paciente paciente = pacientes.stream()
                .filter(p -> p.getDocumento().equals(documento))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Paciente não cadastrado!"));

        LocalDateTime data = LocalDateTime.parse(dataHora);

        consultas.add(new Consulta(medico, paciente, data));
    }

    public synchronized List<Consulta> listarConsultas() {
        return List.copyOf(consultas);
    }

    public synchronized void cancelarConsulta(int indice) {
        if (indice < 0 || indice >= consultas.size()) {
            throw new IllegalArgumentException(
                    "Consulta não encontrada!");
        }

        consultas.get(indice).cancelar();
    }
}
