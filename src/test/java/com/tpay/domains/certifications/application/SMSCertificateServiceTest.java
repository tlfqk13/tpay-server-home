package com.tpay.domains.certifications.application;

import java.io.IOException;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class SMSCertificateServiceTest {

  private static MockWebServer mockWebServer;

  @BeforeAll
  public static void setup() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  public static void teardown() throws IOException {
    mockWebServer.shutdown();
  }
}