package com.lgsoftworks.emailverification.infrastructure.adapter.out.email;

import com.lgsoftworks.emailverification.domain.port.out.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailSenderAdapter implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String email, String code) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Verificación de correo - Fitora");

            String html = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    </head>

                    <body style="
                        margin: 0;
                        padding: 0;
                        background-color: #f4f4f5;
                        font-family: Arial, Helvetica, sans-serif;
                    ">

                        <div style="
                            max-width: 600px;
                            margin: 40px auto;
                            background-color: #ffffff;
                            border-radius: 12px;
                            overflow: hidden;
                            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
                        ">

                            <!-- Header -->
                            <div style="
                                background-color: #18181b;
                                padding: 28px;
                                text-align: center;
                            ">
                                <h1 style="
                                    margin: 0;
                                    color: #ffffff;
                                    font-size: 28px;
                                    letter-spacing: 1px;
                                ">
                                    Fitora
                                </h1>
                            </div>

                            <!-- Content -->
                            <div style="padding: 40px 35px;">

                                <h2 style="
                                    margin-top: 0;
                                    color: #18181b;
                                    font-size: 22px;
                                ">
                                    Verifica tu correo
                                </h2>

                                <p style="
                                    color: #52525b;
                                    font-size: 15px;
                                    line-height: 1.6;
                                ">
                                    Hola,
                                </p>

                                <p style="
                                    color: #52525b;
                                    font-size: 15px;
                                    line-height: 1.6;
                                ">
                                    Hemos recibido una solicitud para verificar tu
                                    dirección de correo electrónico.
                                </p>

                                <p style="
                                    color: #52525b;
                                    font-size: 15px;
                                    line-height: 1.6;
                                ">
                                    Utiliza el siguiente código:
                                </p>

                                <!-- Verification code -->
                                <div style="
                                    margin: 30px 0;
                                    text-align: center;
                                ">
                                    <span style="
                                        display: inline-block;
                                        background-color: #f4f4f5;
                                        color: #18181b;
                                        font-size: 32px;
                                        font-weight: bold;
                                        letter-spacing: 8px;
                                        padding: 18px 28px;
                                        border-radius: 10px;
                                    ">
                                        %s
                                    </span>
                                </div>

                                <p style="
                                    color: #71717a;
                                    font-size: 14px;
                                    line-height: 1.6;
                                    text-align: center;
                                ">
                                    Este código es válido durante
                                    <strong>10 minutos</strong>.
                                </p>

                                <hr style="
                                    border: none;
                                    border-top: 1px solid #e4e4e7;
                                    margin: 30px 0;
                                ">

                                <p style="
                                    color: #71717a;
                                    font-size: 13px;
                                    line-height: 1.6;
                                ">
                                    Si no solicitaste este código, puedes ignorar
                                    este correo. Tu cuenta permanecerá segura.
                                </p>

                            </div>

                            <!-- Footer -->
                            <div style="
                                background-color: #fafafa;
                                padding: 20px;
                                text-align: center;
                            ">
                                <p style="
                                    margin: 0;
                                    color: #a1a1aa;
                                    font-size: 12px;
                                ">
                                    © 2026 Fitora. Todos los derechos reservados.
                                </p>
                            </div>

                        </div>

                    </body>
                    </html>
                    """.formatted(code);

            helper.setText(html, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(
                    "Error sending verification email",
                    e
            );
        }
    }
}