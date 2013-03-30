package com.farpost.idea.beans.php;

import com.farpost.idea.beans.Patterns;
import com.google.common.base.Predicate;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static com.farpost.idea.beans.php.Util.TO_SETTER_OR_GETTER_LOOKUP;
import static com.farpost.idea.beans.php.Util.setterOrGetterToField;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

/**
 * User: zolotov
 * Date: 12/2/12
 */
public class BeanPropertyReferenceProvider extends PsiReferenceProvider {
  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
    return element instanceof XmlAttributeValue
           ? new PsiReference[]{new PhpSetterReference((XmlAttributeValue)element)}
           : PsiReference.EMPTY_ARRAY;
  }

  private static class PhpSetterReference extends PsiPolyVariantReferenceBase<XmlAttributeValue> {
    public PhpSetterReference(XmlAttributeValue psiElement) {
      super(psiElement);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
      return PsiElementResolveResult.createResults(filter(getBeanSetters(), new Predicate<PsiNamedElement>() {
        @Override
        public boolean apply(PsiNamedElement psiNamedElement) {
          return getValue().equals(setterOrGetterToField(psiNamedElement.getName()));
        }
      }));
    }

    @NotNull
    @Override
    public Object[] getVariants() {
      return transform(getBeanSetters(), TO_SETTER_OR_GETTER_LOOKUP).toArray(LookupElement.EMPTY_ARRAY);
    }

    @NotNull
    @Override
    public String getCanonicalText() {
      return getValue();
    }

    private Collection<PsiNamedElement> getBeanSetters() {
      Collection<PsiNamedElement> result = newArrayList();
      final PhpClass phpClass = getPhpClassOfParentBean();
      if (phpClass != null) {
        final Collection<Method> methods = phpClass.getMethods();
        for (Method method : methods) {
          final String methodName = method.getName();
          if (Util.isSetter(methodName)) {
            result.add(method);
          }
        }
      }
      return result;
    }

    @Nullable
    private PhpClass getPhpClassOfParentBean() {
      final XmlAttributeValue classAttributeValue = getBeanClassAttributeValue();
      if (classAttributeValue != null) {
        final PsiReference reference = classAttributeValue.getReference();
        if (reference != null) {
          final PsiElement result = reference.resolve();
          if (result instanceof PhpClass) {
            return (PhpClass)result;
          }
        }
      }
      return null;
    }

    @Nullable
    private XmlAttributeValue getBeanClassAttributeValue() {
      final PsiElement propertyTag = PsiTreeUtil.getParentOfType(myElement, XmlTag.class);
      final XmlTag beanTag = PsiTreeUtil.getParentOfType(propertyTag, XmlTag.class);
      if (beanTag != null && Patterns.BEAN_TAG_NAME.equals(beanTag.getLocalName())) {
        final XmlAttribute classAttribute = beanTag.getAttribute(Patterns.CLASS_ATTRIBUTE_NAME);
        return classAttribute != null
               ? classAttribute.getValueElement()
               : null;
      }
      return null;
    }
  }
}

