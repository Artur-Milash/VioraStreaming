package org.viora.viorastreamingcore.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.TimeZone;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BaseIntegrationTest {
  static {
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kyiv"));
  }
}