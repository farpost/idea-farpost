package com.farpost.idea.beans.php;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
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
      final Project project = getElement().getProject();
      final String fqn = getValue().toLowerCase();
      return PsiElementResolveResult.createResults(PhpIndex.getInstance(project).getClassesByFQN(fqn));
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

