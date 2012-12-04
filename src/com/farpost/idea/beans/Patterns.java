package com.farpost.idea.beans;

import com.intellij.patterns.XmlAttributeValuePattern;
import com.intellij.patterns.XmlFilePattern;

import static com.intellij.patterns.XmlPatterns.*;

/**
 * User: zolotov
 * Date: 12/4/12
 */
public class Patterns {
  private static final String CLASS_ATTRIBUTE_NAME = "class";
  private static final String BEANS_TAG_NAME = "beans";
  private static final String BEANS_XML_NS = "http://farpost.com/injector/schema";
  private static final String TOOLKIT_NS = "http://farpost.com/slr/toolkit/injector-schema";
  private static final String BEAN_TAG_NAME = "bean";

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
}
