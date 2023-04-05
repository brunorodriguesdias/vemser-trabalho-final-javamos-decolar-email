package br.com.dbc.javamosdecolaremail.service;

import br.com.dbc.javamosdecolaremail.dto.EmailUsuarioDTO;
import br.com.dbc.javamosdecolaremail.dto.VendaEmailDTO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailService {

    private final Configuration fmConfiguration;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String template, String email) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Javamos Decolar");
            mimeMessageHelper.setText(template, true);

            emailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            log.info("Erro ao enviar e-mail");
        }
    }

    public void sendEmail(VendaEmailDTO vendaEmailDTO) {
        this.sendEmail(this.getVendaTemplate(vendaEmailDTO), vendaEmailDTO.getComprador().getEmail());
    }

    public void sendEmail(EmailUsuarioDTO emailUsuarioDTO) {
        this.sendEmail(this.getNovoUsuarioTemplate(emailUsuarioDTO), emailUsuarioDTO.getEmail());
    }

    public String getVendaTemplate(VendaEmailDTO vendaEmailDTO) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", vendaEmailDTO.getComprador().getNome());
        dados.put("codigo", vendaEmailDTO.getCodigoVenda());
        dados.put("email", from);

        Template template = null;

        try {
            switch (vendaEmailDTO.getAcao()) {
                case "CRIAR":
                    template = fmConfiguration.getTemplate("venda-realizada-template.ftl");
                    break;
                case "DELETAR":
                    template = fmConfiguration.getTemplate("venda-cancelada-template.ftl");
                    break;
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);

        } catch (TemplateException | RuntimeException | IOException e) {
            log.info("Erro ao enviar e-mail");
            return null;
        }
    }
    public String getNovoUsuarioTemplate(EmailUsuarioDTO emailUsuarioDTO) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", emailUsuarioDTO.getNome());
        dados.put("email", from);

        try {
            Template template = fmConfiguration.getTemplate("usuario-criado-template.ftl");

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);

        } catch (TemplateException | RuntimeException | IOException e) {
            log.info("Erro ao enviar e-mail");
            return null;
        }
    }
}