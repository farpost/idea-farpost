package com.farpost.idea.beans;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.XmlAttributeValuePattern;
import com.intellij.patterns.XmlFilePattern;
import com.intellij.patterns.XmlTagPattern;
import com.intellij.psi.PsiElement;

import static com.intellij.patterns.XmlPatterns.*;

/**
 * User: zolotov
 * Date: 12/4/12
 */
public class Patterns {
  public static final String CLASS_ATTRIBUTE_NAME = "class";
  private static final String BEANS_TAG_NAME = "beans";
  private static final String BEANS_XML_NS = "http://farpost.com/injector/schema";
  private static final String TOOLKIT_NS = "http://farpost.com/slr/toolkit/injector-schema";
  public static final String BEAN_TAG_NAME = "bean";

  public static XmlAttributeValuePattern beanClassPattern() {
    return xmlAttributeValue().withLocalNameIgnoreCase(CLASS_ATTRIBUTE_NAME).inFile(beansXml());
  }

  public static XmlAttributeValuePattern toolkitMethodPattern() {
    return xmlAttributeValue().withLocalNameIgnoreCase("name")
            .withSuperParent(2, xmlTag().withLocalName(BEAN_TAG_NAME).withNamespace(TOOLKIT_NS)).inFile(beansXml());
  }

  public static XmlFilePattern.Capture beansXml() {
    return xmlFile().withRootTag(xmlTag().withLocalName(BEANS_TAG_NAME).withNamespace(BEANS_XML_NS));
  }

  public static XmlTagPattern.Capture beanTag() {
    return xmlTag().withLocalName(BEAN_TAG_NAME).withNamespace(BEANS_XML_NS);
  }

  public static ElementPattern<? extends PsiElement> beanPropertyPattern() {
    return xmlAttributeValue().withLocalNameIgnoreCase("name")
            .withSuperParent(2, xmlTag().withLocalName("property").withParent(beanTag()));
  }

  private Patterns() {
  }
}
