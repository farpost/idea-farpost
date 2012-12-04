package com.farpost.idea.beans.php.reflection;

import com.farpost.idea.beans.php.Util;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.farpost.idea.beans.php.reflection.PhpIndexAdapter.phpIndexClass;

/**
 * User: zolotov
 * Date: 12/4/12
 */
@SuppressWarnings("All")
public class PhpCompletionAdapter {
  private static Class completionContributorProviderClass;
  private static Method addNamespacesMethod;
  private static Class phpLookupElementClass;

  private static Field phpLookupElementField;

  static {
    try {
      completionContributorProviderClass = Class.forName("com.jetbrains.php.completion.PhpCompletionContributor");
      phpLookupElementClass = Class.forName("com.jetbrains.php.completion.PhpLookupElement");
      phpLookupElementField = phpLookupElementClass.getField("handler");
      addNamespacesMethod = completionContributorProviderClass.getDeclaredMethod("addNamespaces", String.class, CompletionResultSet.class, phpIndexClass);
      addNamespacesMethod.setAccessible(true);
    } catch (ClassNotFoundException e) {
      ReflectionUtil.error(e);
    } catch (NoSuchMethodException e) {
      ReflectionUtil.error(e);
    } catch (NoSuchFieldException e) {
      ReflectionUtil.error(e);
    }
  }

  private final PsiElement element;

  public PhpCompletionAdapter(@NotNull PsiElement element) {
    this.element = element;
  }

  public void addClassCompletions(CompletionParameters parameters, CompletionResultSet result) {
    final PhpIndexAdapter phpIndexAdapter = PhpIndexAdapter.getInstance(element.getProject());
    final Pair<String, String> nameAndNamespace = Util.getNameAndNamespace(element.getText());
    try {
      Collection<PsiNamedElement> variants = new HashSet<PsiNamedElement>();
      final String normalizedNamespace = Util.normalizeNamespace(nameAndNamespace.second);
      final PrefixMatcher prefixMatcher = result.getPrefixMatcher().cloneWithPrefix(nameAndNamespace.first);
      result = result.withPrefixMatcher(prefixMatcher);
      addNamespacesMethod.invoke(null, normalizedNamespace, result, phpIndexAdapter.getIndex());
      final boolean restrict = parameters.getInvocationCount() <= 1 && (!nameAndNamespace.second.isEmpty() || result.getPrefixMatcher().getPrefix().length() == 0);
      for (String className : phpIndexAdapter.getAllClassNames(prefixMatcher)) {
        Collection<PsiNamedElement> classesByName = phpIndexAdapter.getClassesByName(className);
        if (restrict) {
          classesByName = phpIndexAdapter.filterByNamespace(classesByName, nameAndNamespace.second.isEmpty() ? null : normalizedNamespace);
        }
        variants.addAll(classesByName);
      }
      final List<LookupElement> list = PhpVariantsUtilAdapter.getLookupItemsForClasses(variants);
      for (LookupElement lookupElement : list) {
        phpLookupElementField.set(lookupElement, null);
      }
      result.addAllElements(list);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
  }
}
