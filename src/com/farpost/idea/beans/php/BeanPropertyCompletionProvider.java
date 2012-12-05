package com.farpost.idea.beans.php;

import com.farpost.idea.beans.BeansPsiUtil;
import com.farpost.idea.beans.php.reflection.PhpClassAdapter;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * User: zolotov
 * Date: 12/5/12
 */
public class BeanPropertyCompletionProvider extends CompletionProvider<CompletionParameters> {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
    final PsiElement phpClass = BeansPsiUtil.getPhpClassOfParentBean(parameters.getPosition());
    if (phpClass != null) {
      final Collection<PsiNamedElement> methods = PhpClassAdapter.getAllMethods(phpClass);
      for (PsiNamedElement method : methods) {
        final String methodName = method.getName();
        if (methodName != null && methodName.startsWith("set")) {
          final String propertyName = methodName.substring(3);
          if (StringUtil.isCapitalized(propertyName)) {
            result.addElement(LookupElementBuilder.create(method, StringUtil.decapitalize(propertyName))
                    .withIcon(AllIcons.Nodes.Field));
          }
        }
      }
    }
  }
}
