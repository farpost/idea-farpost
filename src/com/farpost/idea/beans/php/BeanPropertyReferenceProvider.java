package com.farpost.idea.beans.php;

import com.farpost.idea.beans.php.reflection.PhpClassAdapter;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.farpost.idea.beans.BeansPsiUtil.getPhpClassOfParentBean;

/**
 * User: zolotov
 * Date: 12/2/12
 */
public class BeanPropertyReferenceProvider extends PsiReferenceProvider {

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
    return element instanceof XmlAttributeValue
            ? new PsiReference[]{new PhpSetterReference((XmlAttributeValue) element)}
            : PsiReference.EMPTY_ARRAY;
  }

  private class PhpSetterReference extends PsiPolyVariantReferenceBase<XmlAttributeValue> {
    public PhpSetterReference(XmlAttributeValue psiElement) {
      super(psiElement);
    }

    @NotNull
    @Override
    public String getValue() {
      return "set" + StringUtil.capitalize(myElement.getValue());
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
      final String methodName = getValue();
      final PsiElement phpClass = getPhpClassOfParentBean(myElement);
      if (phpClass != null) {
        final Collection<PsiNamedElement> methods = PhpClassAdapter.getAllMethods(phpClass);
        return PsiElementResolveResult.createResults(Collections2.filter(methods, new Predicate<PsiNamedElement>() {
          @Override
          public boolean apply(PsiNamedElement psiNamedElement) {
            return methodName.equals(psiNamedElement.getName());
          }
        }));
      }
      return ResolveResult.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getCanonicalText() {
      return getValue();
    }

    @NotNull
    @Override
    public Object[] getVariants() {
      /**
       * covered with {@link com.farpost.idea.beans.BeansCompletionContributor}
       */
      return PsiElement.EMPTY_ARRAY;
    }


  }
}

