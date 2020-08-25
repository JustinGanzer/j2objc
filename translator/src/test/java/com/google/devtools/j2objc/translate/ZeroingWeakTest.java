/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.devtools.j2objc.translate;

import com.google.devtools.j2objc.GenerationTest;
import com.google.devtools.j2objc.Options.MemoryManagementOption;
import java.io.IOException;

/** @author Michał Pociecha-Łoś */
public class ZeroingWeakTest extends GenerationTest {

  static final String FIELD_DECLARATION_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string;"
          + "}";

  public void testFieldDeclarationMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        FIELD_DECLARATION_SOURCE,
        "Test",
        "Test.h",
        "JavaLangRefWeakReference *string_;");
  }

  public void testFieldDeclarationArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC,
        FIELD_DECLARATION_SOURCE,
        "Test",
        "Test.h",
        "weak NSString *string_;");
  }

  static final String FIELD_DECLARATION_WITH_INITIALIZER_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string = \"foo\";"
          + "}";

  public void testFieldDeclarationWithInitializerMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        FIELD_DECLARATION_WITH_INITIALIZER_SOURCE,
        "Test",
        "Test.m",
        "JreStrongAssign(&self->string_, JreMakeZeroingWeak(@\"foo\"));");
  }

  public void testFieldDeclarationWithInitializerArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC,
        FIELD_DECLARATION_WITH_INITIALIZER_SOURCE,
        "Test",
        "Test.m",
        "self->string_ = @\"foo\";");
  }

  static final String DIRECT_FIELD_READ_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string;"
          + "  public String run() {"
          + "    return string;"
          + "  }"
          + "}";

  public void testDirectFieldReadMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        DIRECT_FIELD_READ_SOURCE,
        "Test",
        "Test.m",
        "return JreZeroingWeakGet(string_);");
  }

  public void testDirectFieldReadArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC, DIRECT_FIELD_READ_SOURCE, "Test", "Test.m", "return string_;");
  }

  static final String INDIRECT_FIELD_READ_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string;"
          + "  public String run() {"
          + "    return this.string;"
          + "  }"
          + "}";

  public void testIndirectFieldReadMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        INDIRECT_FIELD_READ_SOURCE,
        "Test",
        "Test.m",
        "return JreZeroingWeakGet(self->string_);");
  }

  public void testIndirectFieldReadArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC,
        INDIRECT_FIELD_READ_SOURCE,
        "Test",
        "Test.m",
        "return self->string_;");
  }

  static final String DIRECT_FIELD_WRITE_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string;"
          + "  public void run() { string = \"foo\"; }"
          + "}";

  public void testDirectFieldWriteMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        DIRECT_FIELD_WRITE_SOURCE,
        "Test",
        "Test.m",
        "JreStrongAssign(&string_, JreMakeZeroingWeak(@\"foo\"));");
  }

  public void testDirectFieldWriteArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC,
        DIRECT_FIELD_WRITE_SOURCE,
        "Test",
        "Test.m",
        "string_ = @\"foo\";");
  }

  static final String INDIRECT_FIELD_WRITE_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string;"
          + "  public void run() { this.string = \"foo\"; }"
          + "}";

  public void testIndirectFieldWriteMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        INDIRECT_FIELD_WRITE_SOURCE,
        "Test",
        "Test.m",
        "JreStrongAssign(&self->string_, JreMakeZeroingWeak(@\"foo\"));");
  }

  public void testIndirectFieldWriteArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC,
        INDIRECT_FIELD_WRITE_SOURCE,
        "Test",
        "Test.m",
        "self->string_ = @\"foo\";");
  }

  static final String STRING_CONCAT_SOURCE =
      "import com.google.j2objc.annotations.ZeroingWeak;"
          + "class WeakTest {"
          + "  @ZeroingWeak String string;"
          + "  public String get() { return string + string; }"
          + "}";

  public void testStringConcatMrc() throws IOException {
    testTranslation(
        MemoryManagementOption.REFERENCE_COUNTING,
        STRING_CONCAT_SOURCE,
        "Test",
        "Test.m",
        "return JreStrcat(\"$$\", JreZeroingWeakGet(string_), JreZeroingWeakGet(string_));");
  }

  public void testStringConcatArc() throws IOException {
    testTranslation(
        MemoryManagementOption.ARC,
        STRING_CONCAT_SOURCE,
        "Test",
        "Test.m",
        "return JreStrcat(\"$$\", string_, string_);");
  }

  // DO_NOT_SUBMIT: This test breaks.
  // public void testStringPlusAssign() throws IOException {
  //   String source =
  //       "import com.google.j2objc.annotations.ZeroingWeak;"
  //           + "class WeakTest {"
  //           + "  @ZeroingWeak String string;"
  //           + "  public void run() { string += string; }"
  //           + "}";
  //   String translation = translateSourceFile(source, "Test", "Test.m");
  //   assertTranslation(
  //       translation,
  //       "jreStrongAssign(&self->string_, JreMakeZeroingWeak(JreStrcat(\"$$\","
  //           + " JreZeroingWeakGet(string_), JreZeroingWeakGet(string_)));");
  // }

  private void testTranslation(
      MemoryManagementOption memoryManagementOption,
      String source,
      String typeName,
      String fileName,
      String expected)
      throws IOException {
    options.setMemoryManagementOption(memoryManagementOption);
    String translation = translateSourceFile(source, typeName, fileName);
    assertTranslation(translation, expected);
  }
}
