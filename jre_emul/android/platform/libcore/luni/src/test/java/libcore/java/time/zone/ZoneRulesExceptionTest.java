/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package libcore.java.time.zone;

import junit.framework.TestCase;
import org.junit.Test;
import java.time.zone.ZoneRulesException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ZoneRulesException}.
 */
public class ZoneRulesExceptionTest extends TestCase {

    @Test
    public void test_constructor_message() {
        ZoneRulesException ex = new ZoneRulesException("message");
        assertEquals("message", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void test_constructor_message_cause() {
        Throwable cause = new Exception();
        ZoneRulesException ex = new ZoneRulesException("message", cause);
        assertEquals("message", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
