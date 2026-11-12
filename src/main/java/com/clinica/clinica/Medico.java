package com.clinica.clinica;

public class Medico {
    
    private String nome;
    private final String id = java.util.UUID.randomUUID().toString();
    private String especialidade;

    public Medico(String nome, String especialidade) {
        this.nome = nome;
        this.especialidade = especialidade;
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public String getEspecialidade() {
        return especialidade;
    }
}

