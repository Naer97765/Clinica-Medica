
package com.clinica.clinica;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
            String nome, String especialidade) {

        medicos.add(new Medico(nome, especialidade));
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
            String medicoId, String documento, String dataHora) {

        agendarConsulta(medicoId, documento, dataHora, false);
    }

    public synchronized void agendarConsulta(
            String medicoId, String documento, String dataHora, boolean urgente) {

        Medico medico = medicos.stream()
                .filter(m -> m.getId().equals(medicoId))
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

        LocalDateTime data = LocalDateTime.parse(dataHora).truncatedTo(ChronoUnit.MINUTES);

        boolean horarioOcupado = consultas.stream()
                .anyMatch(consulta -> !consulta.isCancelada()
                        && consulta.getDataHora().truncatedTo(ChronoUnit.MINUTES).equals(data));

        if (horarioOcupado) {
            throw new IllegalArgumentException(
                    "Já existe uma consulta agendada neste dia e horário. Escolha outro horário.");
        }

        consultas.add(new Consulta(medico, paciente, data, urgente));
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
