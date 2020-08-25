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

package com.google.devtools.j2objc.util;

import com.google.devtools.j2objc.ast.Assignment;
import com.google.devtools.j2objc.ast.Expression;
import com.google.devtools.j2objc.ast.TreeNode;

/**
 * Expression utilities.
 *
 * @author Michał Pociecha-Łoś
 */
public final class ExpressionUtil {
  private ExpressionUtil() {}

  public static boolean isAssignmentLeftHandSide(Expression expression) {
    TreeNode parent = expression.getParent();
    return parent instanceof Assignment && ((Assignment) parent).getLeftHandSide() == expression;
  }
}
