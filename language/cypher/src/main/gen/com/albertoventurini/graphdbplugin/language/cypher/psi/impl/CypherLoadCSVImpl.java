/**
 * Copied and adapted from plugin
 * <a href="https://github.com/neueda/jetbrains-plugin-graph-database-support">Graph Database Support</a>
 * by Neueda Technologies, Ltd.
 * Modified by Alberto Venturini, 2022
 */
// This is a generated file. Not intended for manual editing.
package com.albertoventurini.graphdbplugin.language.cypher.psi.impl;

import com.albertoventurini.graphdbplugin.language.cypher.psi.*;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

import static com.albertoventurini.graphdbplugin.language.cypher.psi.CypherTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

public class CypherLoadCSVImpl extends ASTWrapperPsiElement implements CypherLoadCSV {

  public CypherLoadCSVImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CypherVisitor visitor) {
    visitor.visitLoadCSV(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CypherVisitor) accept((CypherVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public CypherExpression getExpression() {
    return findNotNullChildByClass(CypherExpression.class);
  }

  @Override
  @Nullable
  public CypherStringLiteral getStringLiteral() {
    return findChildByClass(CypherStringLiteral.class);
  }

  @Override
  @NotNull
  public CypherVariable getVariable() {
    return findNotNullChildByClass(CypherVariable.class);
  }

  @Override
  @NotNull
  public PsiElement getKAs() {
    return findNotNullChildByType(K_AS);
  }

  @Override
  @NotNull
  public PsiElement getKCsv() {
    return findNotNullChildByType(K_CSV);
  }

  @Override
  @Nullable
  public PsiElement getKFieldterminator() {
    return findChildByType(K_FIELDTERMINATOR);
  }

  @Override
  @NotNull
  public PsiElement getKFrom() {
    return findNotNullChildByType(K_FROM);
  }

  @Override
  @Nullable
  public PsiElement getKHeaders() {
    return findChildByType(K_HEADERS);
  }

  @Override
  @NotNull
  public PsiElement getKLoad() {
    return findNotNullChildByType(K_LOAD);
  }

  @Override
  @Nullable
  public PsiElement getKWith() {
    return findChildByType(K_WITH);
  }

}