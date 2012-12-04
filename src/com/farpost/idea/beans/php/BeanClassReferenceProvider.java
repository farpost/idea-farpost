package com.farpost.idea.beans.php;

import com.farpost.idea.beans.php.reflection.PhpIndexAdapter;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * User: zolotov
 * Date: 12/2/12
 */
public class BeanClassReferenceProvider extends PsiReferenceProvider {

  @NotNull
  @Override
  public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
    return new PsiReference[]{new PhpClassReference(element)};
  }

  private class PhpClassReference extends PsiPolyVariantReferenceBase<PsiElement> {
    public PhpClassReference(PsiElement psiElement) {
      super(psiElement);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
      PhpIndexAdapter indexAdapter = PhpIndexAdapter.getInstance(getElement().getProject());
      return PsiElementResolveResult.createResults(indexAdapter.getClassesByFQN(getValue().toLowerCase()));
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

