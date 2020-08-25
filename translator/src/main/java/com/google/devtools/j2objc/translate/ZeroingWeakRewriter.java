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

package com.google.devtools.j2objc.translate;

import com.google.devtools.j2objc.ast.Assignment;
import com.google.devtools.j2objc.ast.CompilationUnit;
import com.google.devtools.j2objc.ast.Expression;
import com.google.devtools.j2objc.ast.FunctionInvocation;
import com.google.devtools.j2objc.ast.TreeNode;
import com.google.devtools.j2objc.ast.TreeUtil;
import com.google.devtools.j2objc.ast.UnitTreeVisitor;
import com.google.devtools.j2objc.ast.VariableAccess;
import com.google.devtools.j2objc.ast.VariableDeclarationFragment;
import com.google.devtools.j2objc.types.FunctionElement;
import com.google.devtools.j2objc.types.GeneratedVariableElement;
import com.google.devtools.j2objc.util.ElementUtil;
import com.google.devtools.j2objc.util.ExpressionUtil;
import com.google.devtools.j2objc.util.TypeUtil;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;

/**
 * Rewrites variables annotated with @ZeroingWeak.
 *
 * <p>Manual reference counting (MRC): all variables of type T annotated with @ZeroingWeak are
 * converted to WeakReference<T>. All reads from these variables are implicitly de-referenced by
 * calling JreZeroingWeakGet(), and all writes to these variables are implicitly wrapped with
 * JreMakeZeroingWeak().
 *
 * <p>Automatic reference counting (ARC): this rewriter does nothing in ARC. Variables annotated
 * with @ZeroingWeak will be translated to "weak" inside TypeDeclarationGenerator.
 *
 * @author Michał Pociecha-Łoś
 */
public class ZeroingWeakRewriter extends UnitTreeVisitor {

  private static final FunctionElement JRE_ZEROING_WEAK_GET_FUNCTION_ELEMENT =
      new FunctionElement("JreZeroingWeakGet", TypeUtil.ID_TYPE, null)
          .addParameters(TypeUtil.ID_TYPE);

  private static final FunctionElement JRE_MAKE_ZEROING_WEAK_FUNCTION_ELEMENT =
      new FunctionElement("JreMakeZeroingWeak", TypeUtil.ID_TYPE, null)
          .addParameters(TypeUtil.ID_TYPE);

  public ZeroingWeakRewriter(CompilationUnit unit) {
    super(unit);
  }

  @Override
  public void endVisit(VariableDeclarationFragment variableDeclarationFragment) {
    if (options.useARC()) {
      return;
    }

    VariableElement variableElement = variableDeclarationFragment.getVariableElement();
    boolean isZeroingWeak = ElementUtil.isZeroingWeakReference(variableElement);
    if (!isZeroingWeak) {
      return;
    }

    variableDeclarationFragment.setVariableElement(weakReferenceVariableElement(variableElement));
    Expression initializer = variableDeclarationFragment.getInitializer();
    if (initializer != null) {
      replaceWithMakeZeroingWeak(initializer);
    }
  }

  @Override
  public void endVisit(Assignment assignment) {
    if (options.useARC()) {
      return;
    }

    Expression lhsExpression = assignment.getLeftHandSide();
    if (lhsExpression == null) {
      return;
    }

    VariableAccess lhsVariableAccess = TreeUtil.getVariableAccess(lhsExpression);
    if (lhsVariableAccess == null) {
      return;
    }

    VariableElement lhsVariableElement = lhsVariableAccess.getVariableElement();
    boolean isZeroingWeak = ElementUtil.isZeroingWeakReference(lhsVariableElement);
    if (!isZeroingWeak) {
      return;
    }

    VariableElement variableElement = weakReferenceVariableElement(lhsVariableElement);
    lhsVariableAccess.setVariableElement(variableElement);
    replaceWithMakeZeroingWeak(assignment.getRightHandSide());
  }

  @Override
  public void postVisit(TreeNode treeNode) {
    if (options.useARC()) {
      return;
    }

    if (treeNode instanceof Expression) {
      visit((Expression) treeNode);
    }
  }

  private void visit(Expression expression) {
    if (!expression.canReplaceWith(Expression.class)) {
      return;
    }

    if (ExpressionUtil.isAssignmentLeftHandSide(expression)) {
      return;
    }

    VariableAccess variableAccess = TreeUtil.getVariableAccess(expression);
    if (variableAccess == null) {
      return;
    }

    VariableElement variableElement = variableAccess.getVariableElement();
    boolean isZeroingWeak = ElementUtil.isZeroingWeakReference(variableElement);
    if (!isZeroingWeak) {
      return;
    }

    VariableElement weakVariableElement = weakReferenceVariableElement(variableElement);
    variableAccess.setVariableElement(weakVariableElement);
    replaceWithGetZeroingWeak(expression);
  }

  private void replaceWithMakeZeroingWeak(Expression expression) {
    expression.replaceWith(
        () -> {
          DeclaredType weakReferenceType =
              typeUtil.getJavaWeakReference(expression.getTypeMirror());
          FunctionElement functionElement = JRE_MAKE_ZEROING_WEAK_FUNCTION_ELEMENT;
          return new FunctionInvocation(functionElement, weakReferenceType).addArgument(expression);
        });
  }

  private void replaceWithGetZeroingWeak(Expression expression) {
    expression.replaceWith(
        () -> {
          FunctionElement functionElement = JRE_ZEROING_WEAK_GET_FUNCTION_ELEMENT;
          return new FunctionInvocation(functionElement, expression.getTypeMirror())
              .addArgument(expression);
        });
  }

  private VariableElement weakReferenceVariableElement(VariableElement variableElement) {
    // TODO: Remove @ZeroingWeak annotation from the returned variable element.
    return GeneratedVariableElement.newField(
        variableElement.getSimpleName().toString(),
        typeUtil.getJavaWeakReference(variableElement.asType()),
        variableElement.getEnclosingElement());
  }
}
