package com.farpost.idea.beans.php;

import com.farpost.idea.beans.php.reflection.PhpClassAdapter;
import com.farpost.idea.beans.php.reflection.PhpImplementationsProviderAdapter;
import com.farpost.idea.beans.php.reflection.PhpIndexAdapter;
import com.google.common.base.Predicate;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
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
            ? new PsiReference[]{new PhpToolkitMethodReference((XmlAttributeValue) element)}
            : PsiReference.EMPTY_ARRAY;
  }

  private class PhpToolkitMethodReference extends PsiPolyVariantReferenceBase<XmlAttributeValue> {
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

    private Collection<PsiNamedElement> getAllToolkitMethods() {
      final Collection<PsiNamedElement> toolkitInterfaces = PhpIndexAdapter.getInstance(myElement.getProject()).getInterfacesByFQN(TOOLKIT_INTERFACE);
      Collection<PsiNamedElement> toolkitMethods = newArrayList();
      for (PsiNamedElement toolkitInterface : toolkitInterfaces) {
        for (PsiNamedElement toolkit : PhpImplementationsProviderAdapter.getAllImplementations(toolkitInterface)) {
          for (PsiNamedElement method : PhpClassAdapter.getAllMethods(toolkit)) {
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

