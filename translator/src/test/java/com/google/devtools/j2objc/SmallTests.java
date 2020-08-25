/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
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

package com.google.devtools.j2objc;

import com.google.devtools.j2objc.translate.ZeroingWeakTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Returns a suite of all small tests in this package.
 */
public class SmallTests {

  private static final Class<?>[] smallTestClasses =
      new Class<?>[] {
        // AbstractMethodRewriterTest.class,
        // AnnotationRewriterTest.class,
        // AnnotationTest.class,
        // AnonymousClassConverterTest.class,
        // ArrayAccessTest.class,
        // ArrayCreationTest.class,
        // ArrayRewriterTest.class,
        // AutoboxerTest.class,
        // CastResolverTest.class,
        // ClassFileConverterTest.class,
        // ClassFileTest.class,
        // CodeReferenceMapTest.class,
        // ComplexExpressionExtractorTest.class,
        // CompoundTypeTest.class,
        // ConstantBranchPrunerTest.class,
        // DeadCodeEliminatorTest.class,
        // DefaultMethodsTest.class,
        // DestructorGeneratorTest.class,
        // ElementUtilTest.class,
        // EnhancedForRewriterTest.class,
        // EnumRewriterTest.class,
        // ErrorUtilTest.class,
        // ExternalAnnotationInjectorTest.class,
        // FileUtilTest.class,
        // FunctionizerTest.class,
        // GwtConverterTest.class,
        // HeaderImportCollectorTest.class,
        // ImplementationImportCollectorTest.class,
        // InfixExpressionTest.class,
        // InitializationNormalizerTest.class,
        // InnerClassExtractorTest.class,
        // J2ObjCIncompatibleStripperTest.class,
        // J2ObjCTest.class,
        // JavaCloneWriterTest.class,
        // JavacParserTest.class,
        // JavacTreeConverterTest.class,
        // JavadocGeneratorTest.class,
        // JavaToIOSMethodTranslatorTest.class,
        // LambdaExpressionTest.class,
        // LambdaTypeElementAdderTest.class,
        // LineDirectivesTest.class,
        // LiteralGeneratorTest.class,
        // MetadataWriterTest.class,
        // MethodReferenceTest.class,
        // NameTableTest.class,
        // NilCheckResolverTest.class,
        // NumberMethodRewriterTest.class,
        // ObjectiveCHeaderGeneratorTest.class,
        // ObjectiveCImplementationGeneratorTest.class,
        // ObjectiveCSegmentedHeaderGeneratorTest.class,
        // ObjectiveCSourceFileGeneratorTest.class,
        // OcniExtractorTest.class,
        // OperatorRewriterTest.class,
        // OptionsTest.class,
        // OuterReferenceFixerTest.class,
        // OuterReferenceResolverTest.class,
        // PackageInfoLookupTest.class,
        // PackageInfoRewriterTest.class,
        // PackagePrefixesTest.class,
        // PrimitiveArrayTest.class,
        // PrivateDeclarationResolverTest.class,
        // ProGuardUsageParserTest.class,
        // RewriterTest.class,
        // SignatureGeneratorTest.class,
        // StatementGeneratorTest.class,
        // StaticVarRewriterTest.class,
        // SuperMethodInvocationRewriterTest.class,
        // SwitchRewriterTest.class,
        // TypeDeclarationGeneratorTest.class,
        // TypeImplementationGeneratorTest.class,
        // TypeUseAnnotationTest.class,
        // TranslationProcessorTest.class,
        // TranslationUtilTest.class,
        // UnicodeUtilsTest.class,
        // UnsequencedExpressionRewriterTest.class,
        // VarargsRewriterTest.class,
        // VariableRenamerTest.class
        ZeroingWeakTest.class,
      };

  public static Test suite() {
    return new TestSuite(smallTestClasses);
  }
}
