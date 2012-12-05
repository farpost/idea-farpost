package com.farpost.idea.beans;

import com.farpost.idea.beans.php.BeanClassCompletionProvider;
import com.farpost.idea.beans.php.BeanPropertyCompletionProvider;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.psi.xml.XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN;

/**
 * User: zolotov
 * Date: 12/4/12
 */
public class BeansCompletionContributor extends CompletionContributor {
  public BeansCompletionContributor() {
    extend(CompletionType.BASIC, psiElement(XML_ATTRIBUTE_VALUE_TOKEN).withParent(Patterns.beanClassPattern()), new BeanClassCompletionProvider());
    extend(CompletionType.BASIC, psiElement(XML_ATTRIBUTE_VALUE_TOKEN).withParent(Patterns.beanPropertyPattern()), new BeanPropertyCompletionProvider());
  }
}
