package br.com.dbc.javamosdecolaremail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaEmailDTO {

    private String acao;
    private EmailUsuarioDTO comprador;
    private String codigoVenda;
}
