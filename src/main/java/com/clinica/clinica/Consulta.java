package com.clinica.clinica;

import java.time.LocalDateTime;

public class Consulta {

    private Medico medico;
    private Paciente paciente;
    private LocalDateTime dataHora;
    private boolean cancelada;

    public Consulta(Medico medico, Paciente paciente, LocalDateTime dataHora) {
        if (medico == null || paciente == null || dataHora == null) {
            throw new IllegalArgumentException(
                "Médico, paciente e data são obrigatórios!"
            );
        }

        this.medico = medico;
        this.paciente = paciente;
        this.dataHora = dataHora;
        this.cancelada = false;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void cancelar() {
        this.cancelada = true;
    }
}