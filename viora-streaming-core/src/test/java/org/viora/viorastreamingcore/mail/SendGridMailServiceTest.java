package org.viora.viorastreamingcore.mail;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.viora.viorastreamingcore.mail.messages.EmailMessage;
import org.viora.viorastreamingcore.mail.services.SendGridMailService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendGridMailServiceTest {

  @Mock
  private JavaMailSender mailSender;

  @Mock
  private TemplateEngine templateEngine;

  @InjectMocks
  private SendGridMailService service;

  private MimeMessage mimeMessage;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "mailFrom", "noreply@viora.com");
    mimeMessage = mock(MimeMessage.class);
  }

  @Test
  void givenEmailMessage_whenSendEmail_thenMailIsSent() {

    Context context = new Context();

    EmailMessage emailMessage = new EmailMessage("emails/base-mail-layout") {
      @Override
      protected Context getContext() {
        return context;
      }
    };

    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(templateEngine.process(eq("emails/base-mail-layout"), eq(context)))
        .thenReturn("<html>body</html>");

    service.sendEmail("user@test.com", emailMessage);

    verify(mailSender).createMimeMessage();
    verify(templateEngine).process("emails/base-mail-layout", context);
    verify(mailSender).send(mimeMessage);
  }

  @Test
  void givenEmailMessage_whenSendEmail_thenTemplateIsProcessedCorrectly() {

    Context context = new Context();

    EmailMessage emailMessage = new EmailMessage("emails/base-mail-layout") {
      @Override
      protected Context getContext() {
        return context;
      }
    };

    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(templateEngine.process(anyString(), any(Context.class)))
        .thenReturn("<html/>");

    service.sendEmail("user@test.com", emailMessage);

    verify(templateEngine).process("emails/base-mail-layout", context);
  }

  @Test
  void givenEmailMessage_whenSendEmail_thenMimeMessageIsSent() {

    Context context = new Context();

    EmailMessage emailMessage = new EmailMessage("emails/base-mail-layout") {
      @Override
      protected Context getContext() {
        return context;
      }
    };

    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(templateEngine.process(anyString(), any(Context.class)))
        .thenReturn("<html/>");

    service.sendEmail("user@test.com", emailMessage);

    verify(mailSender).send(mimeMessage);
  }
}