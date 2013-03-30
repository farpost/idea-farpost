package com.farpost.idea.common;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.rename.NameSuggestionProvider;
import com.intellij.refactoring.rename.PreferrableNameSuggestionProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.Parameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * User: zolotov
 * Date: 3/27/13
 */
public class PhpCustomCompletionContributor extends CompletionContributor {

  @Override
  public void fillCompletionVariants(CompletionParameters parameters, CompletionResultSet result) {
    super.fillCompletionVariants(parameters, result);
  }

  public PhpCustomCompletionContributor() {
    extend(CompletionType.BASIC, parameterName(), new TypeHintSuggestionsProvider());
  }

  private static PsiElementPattern.Capture<? extends PsiElement> parameterName() {
    return psiElement().withParent(Parameter.class);
  }

  @Override
  public void beforeCompletion(@NotNull CompletionInitializationContext context) {
    // completion for parameters without '$'. Very brittle, can break other completions,
    // that not expected replaced dummy_identifier.

    /*final PsiFile file = context.getFile();
    if (file instanceof PhpPsiElement) {
      final int offset = context.getStartOffset();
      PsiElement elementAt = file.findElementAt(offset);
      if (elementAt == null) {
        return;
      }
      if (StringUtil.startsWithChar(elementAt.getText(), '$')) {
        return; //skip variable
      }


      PsiElement prevLeaf = PsiTreeUtil.prevVisibleLeaf(elementAt);
      if (prevLeaf != null) {
        PsiElement parent = prevLeaf.getParent();
        if (parent instanceof ClassReference && parent.getParent() instanceof Parameter &&
            offset > 0 &&
            context.getEditor().getDocument().getCharsSequence().charAt(offset - 1) == ' ') {
          context.setDummyIdentifier("$" + CompletionInitializationContext.DUMMY_IDENTIFIER);
        }
      }
    }*/
  }

  private static class TypeHintSuggestionsProvider extends CompletionProvider<CompletionParameters> {
    public static final char NAMESPACE_SEPARATOR = '\\';
    public static final char VARIABLE_PREFIX = '$';

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters,
                                  ProcessingContext processingContext,
                                  @NotNull CompletionResultSet resultSet) {
      final PsiElement position = parameters.getPosition();
      final Parameter parameter = PsiTreeUtil.getParentOfType(position, Parameter.class);
      if (parameter != null) {
        Collection<String> names = suggestNamesByType(parameter, position);
        final String positionText = position.getText();
        for (String name : names) {
          if (!StringUtil.containsChar(name, NAMESPACE_SEPARATOR)) {
            final String lookupName = VARIABLE_PREFIX + name;
            if (!positionText.equals(lookupName)) {
              resultSet.addElement(LookupElementBuilder.create(lookupName));
            }
          }
        }
      }
    }

    private static Collection<String> suggestNamesByType(@NotNull Parameter parameter, @Nullable PsiElement context) {
      final Set<String> result = newHashSet();
      for (NameSuggestionProvider provider : Extensions.getExtensions(NameSuggestionProvider.EP_NAME)) {
        final SuggestedNameInfo suggestedNameInfo = provider.getSuggestedNames(parameter, context, result);
        if (suggestedNameInfo != null &&
            provider instanceof PreferrableNameSuggestionProvider &&
            !((PreferrableNameSuggestionProvider)provider).shouldCheckOthers()) {
          break;
        }
      }
      return result;
    }
  }
}
