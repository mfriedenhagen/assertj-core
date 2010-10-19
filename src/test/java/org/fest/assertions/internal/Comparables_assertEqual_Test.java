/*
 * Created on Oct 17, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ErrorWhenObjectsAreNotEqual.errorWhenNotEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Comparables#assertEqual(AssertionInfo, Comparable, Comparable)}</code>.
 *
 * @author Alex Ruiz
 */
public class Comparables_assertEqual_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Comparables comparables;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    comparables = new Comparables(failures);
  }

  @Test public void should_throw_error_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    comparables.assertEqual(info, null, 8);
  }

  @Test public void should_pass_if_objects_are_equal() {
    BigDecimal a = new BigDecimal("10.0");
    BigDecimal e = new BigDecimal("10.000");
    // we use BigDecimal to ensure that 'compareTo' is being called, since BigDecimal is the only Comparable where
    // 'compareTo' is not consistent with 'equals'
    assertFalse(a.equals(e));
    comparables.assertEqual(info, a, e);
  }

  @Test public void should_fail_if_objects_are_not_equal() {
    String e = "Yoda";
    String a = "Luke";
    thrown.expectAssertionErrorButNotFromMockito();
    comparables.assertEqual(info, a, e);
    verify(failures).failure(info, errorWhenNotEqual(a, e));
  }
}