/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 */
// This is a generated file. Not intended for manual editing.
package com.albertoventurini.graphdbplugin.language.cypher.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.albertoventurini.graphdbplugin.language.cypher.references.CypherVariableElement;
import com.intellij.psi.PsiReference;

public interface CypherVariable extends CypherVariableElement {

  @NotNull
  CypherSymbolicNameString getSymbolicNameString();

  String getName();

  CypherVariable setName(String newName);

  PsiElement getNameIdentifier();

  @NotNull
  PsiReference[] getReferences();

}