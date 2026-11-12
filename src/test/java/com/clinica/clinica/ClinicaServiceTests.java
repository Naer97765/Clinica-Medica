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
        service.cadastrarMedico("Ana", "Clínica geral");
        service.cadastrarMedico("Bruno", "Cardiologia");
        service.cadastrarPaciente("Carlos", "123");
        service.cadastrarPaciente("Diana", "456");
    }

    @Test
    void recusaMesmoMedicoNoMesmoMinutoComPacientesESegundosDiferentes() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T09:30:01");
        var erro = assertThrows(IllegalArgumentException.class,
                () -> service.agendarConsulta(service.listarMedicos().get(0).getId(), "456", "2026-12-01T09:30:59"));
        assertTrue(erro.getMessage().contains("Este médico já possui uma consulta"));
        assertEquals(1, service.listarConsultas().size());
        assertEquals(0, service.listarConsultas().get(0).getDataHora().getSecond());
    }

    @Test
    void permiteMedicosDeAreasDiferentesNoMesmoHorarioInclusiveUrgencia() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T16:02");
        service.agendarConsulta(service.listarMedicos().get(1).getId(), "456", "2026-12-01T16:02", true);
        assertEquals(2, service.listarConsultas().size());
        assertTrue(service.listarConsultas().get(1).isUrgente());
    }

    @Test
    void bloqueioEhPorProfissionalMesmoComEspecialidadeENomeIguais() {
        service.cadastrarMedico("Ana", "Clínica geral");
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T16:02");
        service.agendarConsulta(service.listarMedicos().get(2).getId(), "456", "2026-12-01T16:02");
        assertEquals(2, service.listarConsultas().size());
    }

    @Test
    void permiteOutroMinutoOutraHoraEOutroDia() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T09:30");
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T09:31");
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T10:30");
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-02T09:30");
        assertEquals(4, service.listarConsultas().size());
    }

    @Test
    void cancelamentoLiberaHorario() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T09:30");
        service.cancelarConsulta(0);
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "456", "2026-12-01T09:30");
        assertTrue(service.listarConsultas().get(0).isCancelada());
        assertFalse(service.listarConsultas().get(1).isCancelada());
        assertThrows(IllegalArgumentException.class,
                () -> service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T09:30"));
    }

    @Test
    void paginaInicialRecebeMedicosEConsultasInclusiveCanceladas() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T09:30");
        service.cancelarConsulta(0);
        var model = new ExtendedModelMap();
        assertEquals("index", new HomeController(service).inicio(model));
        assertEquals(service.listarMedicos(), model.get("medicos"));
        assertEquals(service.listarConsultas(), model.get("consultas"));
    }

    @Test
    void urgenciaMantemBloqueioDeHorarioEIdentificaPrioridade() {
        String id = service.listarMedicos().get(0).getId();
        service.agendarConsulta(id, "123", "2026-12-01T09:30", true);
        assertTrue(service.listarConsultas().get(0).isUrgente());
        assertThrows(IllegalArgumentException.class,
                () -> service.agendarConsulta(id, "456", "2026-12-01T09:30", true));
        service.agendarConsulta(id, "456", "2026-12-01T09:31");
        assertFalse(service.listarConsultas().get(1).isUrgente());
    }

    @Test
    void medicosComMesmoNomeRecebemIdentificadoresDiferentes() {
        service.cadastrarMedico("Ana", "Cardiologia");
        var medicos = service.listarMedicos();
        assertNotEquals(medicos.get(0).getId(), medicos.get(2).getId());
        service.agendarConsulta(medicos.get(2).getId(), "123", "2026-12-01T11:00");
        assertSame(medicos.get(2), service.listarConsultas().get(0).getMedico());
    }

    @Test
    void recusaMesmoPacienteComMedicosDiferentesNoMesmoMinuto() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T16:02:01");
        for (boolean urgente : new boolean[] {false, true}) {
            var erro = assertThrows(IllegalArgumentException.class,
                    () -> service.agendarConsulta(service.listarMedicos().get(1).getId(),
                            "123", "2026-12-01T16:02:59", urgente));
            assertTrue(erro.getMessage().contains("Este paciente já possui uma consulta"));
        }
        assertEquals(1, service.listarConsultas().size());
    }

    @Test
    void cancelamentoLiberaPacienteParaOutroMedicoNoMesmoHorario() {
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T16:02");
        service.cancelarConsulta(0);
        service.agendarConsulta(service.listarMedicos().get(1).getId(), "123", "2026-12-01T16:02", true);
        assertEquals(2, service.listarConsultas().size());
        assertFalse(service.listarConsultas().get(1).isCancelada());
    }

    @Test
    void pacientesHomonimosComDocumentosDiferentesPodemSerAtendidosAoMesmoTempo() {
        service.cadastrarPaciente("Carlos", "789");
        service.agendarConsulta(service.listarMedicos().get(0).getId(), "123", "2026-12-01T16:02");
        service.agendarConsulta(service.listarMedicos().get(1).getId(), "789", "2026-12-01T16:02");
        assertEquals(2, service.listarConsultas().size());
    }

    @Test
    void paginaInicialRecebeListasVazias() {
        var model = new ExtendedModelMap();
        new HomeController(new ClinicaService()).inicio(model);
        assertEquals(java.util.List.of(), model.get("medicos"));
        assertEquals(java.util.List.of(), model.get("consultas"));
    }
}
