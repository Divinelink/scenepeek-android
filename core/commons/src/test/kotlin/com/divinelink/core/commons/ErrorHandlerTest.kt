package com.divinelink.core.commons

import com.divinelink.core.commons.exception.InvalidStatusException
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException

class ErrorHandlerTest {

  @Test
  fun `test on 500 error code`() {
    var actionFor500Invoked = false
    var actionFor404Invoked = false
    var actionForOtherErrorInvoked = false

    ErrorHandler.create(InvalidStatusException(500))
      .on(500) {
        actionFor500Invoked = true
      }
      .on(404) {
        actionFor404Invoked = true
      }
      .otherwise {
        actionForOtherErrorInvoked = true
      }
      .handle()

    assertThat(actionFor500Invoked).isTrue()
    assertThat(actionForOtherErrorInvoked).isFalse()
    assertThat(actionFor404Invoked).isFalse()
  }

  @Test
  fun `test on 404 error code`() {
    var actionFor500Invoked = false
    var actionFor404Invoked = false
    var actionForOtherErrorInvoked = false

    ErrorHandler.create(InvalidStatusException(404))
      .on(500) {
        actionFor500Invoked = true
      }
      .on(404) {
        actionFor404Invoked = true
      }
      .otherwise {
        actionForOtherErrorInvoked = true
      }
      .handle()

    assertThat(actionFor500Invoked).isFalse()
    assertThat(actionFor404Invoked).isTrue()
    assertThat(actionForOtherErrorInvoked).isFalse()
  }

  @Test
  fun `test on other error code`() {
    var actionFor500Invoked = false
    var actionFor404Invoked = false
    var actionForOtherErrorInvoked = false

    ErrorHandler.create(InvalidStatusException(400))
      .on(500) {
        actionFor500Invoked = true
      }
      .on(404) {
        actionFor404Invoked = true
      }
      .otherwise {
        actionForOtherErrorInvoked = true
      }
      .handle()

    assertThat(actionFor500Invoked).isFalse()
    assertThat(actionFor404Invoked).isFalse()
    assertThat(actionForOtherErrorInvoked).isTrue()
  }

  @Test
  fun `test on 500 without correct http error code`() {
    var actionFor500Invoked = false
    var actionFor404Invoked = false

    ErrorHandler.create(Exception("Some error with code 500"))
      .on(500) {
        actionFor500Invoked = true
      }
      .on(404) {
        actionFor404Invoked = true
      }
      .handle()

    assertThat(actionFor500Invoked).isFalse()
    assertThat(actionFor404Invoked).isFalse()
  }

  @Test
  fun `test action is not invoked when desired exception is not thrown`() {
    var actionForConnectExceptionInvoked = false
    var actionForFooExceptionInvoked = false

    ErrorHandler.create(Exception("Error"))
      .on<ConnectException> {
        actionForConnectExceptionInvoked = true
      }
      .on<FooException> {
        actionForFooExceptionInvoked = true
      }
      .handle()

    assertThat(actionForConnectExceptionInvoked).isFalse()
    assertThat(actionForFooExceptionInvoked).isFalse()
  }

  @Test
  fun `test action is invoked when desired exception is thrown`() {
    var actionForConnectExceptionInvoked = false
    var actionForFooExceptionInvoked = false

    ErrorHandler.create(ConnectException())
      .on<ConnectException> {
        actionForConnectExceptionInvoked = true
      }
      .on<FooException> {
        actionForFooExceptionInvoked = true
      }
      .handle()

    assertThat(actionForConnectExceptionInvoked).isTrue()
    assertThat(actionForFooExceptionInvoked).isFalse()
  }

  @Test
  fun `test action is invoked when desired exception is thrown and there is a cause`() {
    var actionForConnectExceptionInvoked = false
    var actionForFooExceptionInvoked = false
    var otherwiseActionInvoked = false

    ErrorHandler.create(Exception(UnknownHostException()))
      .on<ConnectException> {
        actionForConnectExceptionInvoked = true
      }
      .on<FooException> {
        actionForFooExceptionInvoked = true
      }
      .otherwise {
        otherwiseActionInvoked = true
      }
      .handle()

    assertThat(actionForConnectExceptionInvoked).isFalse()
    assertThat(actionForFooExceptionInvoked).isFalse()
    assertThat(otherwiseActionInvoked).isTrue()
  }
}

private class FooException : RuntimeException()
