package com.farpost.idea.beans;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

/**
 * User: zolotov
 * Date: 12/5/12
 */
public class BeansPsiUtil {
  @Nullable
  public static PsiElement getPhpClassOfParentBean(PsiElement child) {
    final XmlAttributeValue classAttributeValue = getBeanClassAttributeValue(child);
    if (classAttributeValue != null) {
      final PsiReference reference = classAttributeValue.getReference();
      if (reference != null) {
        return reference.resolve();
      }
    }
    return null;
  }

  @Nullable
  public static XmlAttributeValue getBeanClassAttributeValue(PsiElement child) {
    final PsiElement propertyTag = PsiTreeUtil.getParentOfType(child, XmlTag.class);
    final XmlTag beanTag = PsiTreeUtil.getParentOfType(propertyTag, XmlTag.class);
    if (beanTag != null && Patterns.BEAN_TAG_NAME.equals(beanTag.getLocalName())) {
      final XmlAttribute classAttribute = beanTag.getAttribute(Patterns.CLASS_ATTRIBUTE_NAME);
      return classAttribute != null
              ? classAttribute.getValueElement()
              : null;
    }
    return null;
  }

  private BeansPsiUtil() {
  }
}
