package com.clinica.clinica;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

class ClinicaServiceTests {
    private ClinicaService service;

    @BeforeEach
    void prepararCadastros() {
        service = new ClinicaService();
        service.cadastrarMedico("Ana", "1", "Clínica geral");
        service.cadastrarMedico("Bruno", "2", "Cardiologia");
        service.cadastrarPaciente("Carlos", "123");
        service.cadastrarPaciente("Diana", "456");
    }

    @Test
    void recusaMesmoMinutoMesmoComMedicoPacienteESegundosDiferentes() {
        service.agendarConsulta("1", "123", "2026-12-01T09:30:01");
        var erro = assertThrows(IllegalArgumentException.class,
                () -> service.agendarConsulta("2", "456", "2026-12-01T09:30:59"));
        assertTrue(erro.getMessage().contains("Já existe uma consulta"));
        assertEquals(1, service.listarConsultas().size());
        assertEquals(0, service.listarConsultas().get(0).getDataHora().getSecond());
    }

    @Test
    void permiteOutroMinutoOutraHoraEOutroDia() {
        service.agendarConsulta("1", "123", "2026-12-01T09:30");
        service.agendarConsulta("1", "123", "2026-12-01T09:31");
        service.agendarConsulta("1", "123", "2026-12-01T10:30");
        service.agendarConsulta("1", "123", "2026-12-02T09:30");
        assertEquals(4, service.listarConsultas().size());
    }

    @Test
    void cancelamentoLiberaHorario() {
        service.agendarConsulta("1", "123", "2026-12-01T09:30");
        service.cancelarConsulta(0);
        service.agendarConsulta("2", "456", "2026-12-01T09:30");
        assertTrue(service.listarConsultas().get(0).isCancelada());
        assertFalse(service.listarConsultas().get(1).isCancelada());
        assertThrows(IllegalArgumentException.class,
                () -> service.agendarConsulta("1", "123", "2026-12-01T09:30"));
    }

    @Test
    void paginaInicialRecebeMedicosEConsultasInclusiveCanceladas() {
        service.agendarConsulta("1", "123", "2026-12-01T09:30");
        service.cancelarConsulta(0);
        var model = new ExtendedModelMap();
        assertEquals("index", new HomeController(service).inicio(model));
        assertEquals(service.listarMedicos(), model.get("medicos"));
        assertEquals(service.listarConsultas(), model.get("consultas"));
    }

    @Test
    void paginaInicialRecebeListasVazias() {
        var model = new ExtendedModelMap();
        new HomeController(new ClinicaService()).inicio(model);
        assertEquals(java.util.List.of(), model.get("medicos"));
        assertEquals(java.util.List.of(), model.get("consultas"));
    }
}
