package com.farpost.idea.beans.php;

import com.google.common.base.Predicate;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.farpost.idea.beans.php.Util.*;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

/**
 * User: zolotov
 * Date: 12/2/12
 */
public class BeanToolkitMethodReferenceProvider extends PsiReferenceProvider {

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
    return element instanceof XmlAttributeValue
           ? new PsiReference[]{new PhpToolkitMethodReference((XmlAttributeValue)element)}
           : PsiReference.EMPTY_ARRAY;
  }

  private static class PhpToolkitMethodReference extends PsiPolyVariantReferenceBase<XmlAttributeValue> {
    private static final String TOOLKIT_INTERFACE = "\\IToolkit";

    public PhpToolkitMethodReference(XmlAttributeValue psiElement) {
      super(psiElement);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
      return PsiElementResolveResult.createResults(filter(getAllToolkitMethods(), new Predicate<PsiNamedElement>() {
        @Override
        public boolean apply(PsiNamedElement psiNamedElement) {
          return getValue().equals(setterOrGetterToField(psiNamedElement.getName()));
        }
      }));
    }

    @NotNull
    @Override
    public Object[] getVariants() {
      return transform(getAllToolkitMethods(), TO_SETTER_OR_GETTER_LOOKUP).toArray(LookupElement.EMPTY_ARRAY);
    }

    private Collection<Method> getAllToolkitMethods() {
      final PhpIndex index = PhpIndex.getInstance(myElement.getProject());
      final Collection<PhpClass> toolkitInterfaces = index.getInterfacesByFQN(TOOLKIT_INTERFACE);
      Collection<Method> toolkitMethods = newArrayList();
      for (PhpClass toolkitInterface : toolkitInterfaces) {
        for (PhpClass toolkit : index.getAllSubclasses(toolkitInterface.getFQN())) {
          for (Method method : toolkit.getMethods()) {
            if (isGetter(method.getName())) {
              toolkitMethods.add(method);
            }
          }
        }
      }
      return toolkitMethods;
    }
  }
}

