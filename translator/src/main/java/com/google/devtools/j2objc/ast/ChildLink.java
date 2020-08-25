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

package com.google.devtools.j2objc.ast;

import java.util.function.Function;

/**
 * A link between a parent and child node that allows for efficient swapping of
 * nodes and handles reparenting of the old and new node when setting a child.
 */
class ChildLink<T extends TreeNode> {

  private final Class<T> childType;
  private final TreeNode parent;
  private T child = null;

  public ChildLink(Class<T> childType, TreeNode parent) {
    this.childType = childType;
    this.parent = parent;
  }

  public static <T extends TreeNode> ChildLink<T> create(Class<T> childType, TreeNode parent) {
    return new ChildLink<T>(childType, parent);
  }

  public Class<T> getChildType() {
    return childType;
  }

  public TreeNode getParent() {
    return parent;
  }

  public T get() {
    return child;
  }

  public void set(T newChild) {
    if (child == newChild) {
      return;
    }
    update(oldChild -> newChild);
  }

  public void update(Function<? super T, ? extends T> function) {
    if (child != null) {
      child.setOwner(null);
    }
    T newChild = function.apply(child);
    if (newChild != null) {
      newChild.setOwner(this);
    }
    child = newChild;
  }

  public void remove() {
    set(null);
  }

  public void setDynamic(TreeNode newChild) {
    updateDynamic(child -> newChild);
  }

  @SuppressWarnings("unchecked")
  public void updateDynamic(Function<TreeNode, TreeNode> function) {
    update(
        child -> {
          TreeNode newChild = function.apply(child);
          assert newChild == null || childType.isInstance(newChild)
              : "Cannot assign node of type "
                  + newChild.getClass().getName()
                  + " to child of type "
                  + childType.getName();
          return (T) newChild;
        });
  }

  @SuppressWarnings("unchecked")
  public void copyFrom(T other) {
    set(other != null ? (T) other.copy() : null);
  }

  public void accept(TreeVisitor visitor) {
    if (child != null) {
      child.accept(visitor);
    }
  }

  @Override
  public String toString() {
    return child != null ? child.toString() : "null";
  }
}
