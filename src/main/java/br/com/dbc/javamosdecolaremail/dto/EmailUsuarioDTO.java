package br.com.dbc.javamosdecolaremail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailUsuarioDTO {
    private String email;
    private String nome;
}