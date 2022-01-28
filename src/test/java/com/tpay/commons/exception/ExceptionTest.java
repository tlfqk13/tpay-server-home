package com.tpay.commons.exception;


import com.tpay.commons.exception.detail.InvalidBusinessNumberException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {"local"})
class ExceptionTest {

  @Test
  public void InvalidBusinessNumberException을발생한다() {
    throw new InvalidBusinessNumberException(ExceptionState.INVALID_BUSINESS_NUMBER,"aa");
  }
}

