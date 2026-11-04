package com.clinica.clinica;

public class Paciente {
 
    private String nome;
    private String documento;

    public Paciente(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }
}

