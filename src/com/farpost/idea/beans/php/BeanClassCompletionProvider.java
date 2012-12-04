package com.farpost.idea.beans.php;

import com.farpost.idea.beans.php.reflection.PhpCompletionAdapter;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
* User: zolotov
* Date: 12/4/12
*/
public class BeanClassCompletionProvider extends CompletionProvider<CompletionParameters> {
  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
    final PsiElement originalPosition = parameters.getOriginalPosition();
    if (originalPosition != null) {
      final PhpCompletionAdapter phpCompletionAdapter = new PhpCompletionAdapter(originalPosition);
      phpCompletionAdapter.addClassCompletions(parameters, result);
    }
  }
}
