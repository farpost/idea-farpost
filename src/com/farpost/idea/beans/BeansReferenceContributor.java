package com.farpost.idea.beans;

import com.farpost.idea.beans.php.BeanClassReferenceProvider;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;

/**
 * User: zolotov
 * Date: 12/2/12
 */
public class BeansReferenceContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(Patterns.beanClassPattern(), new BeanClassReferenceProvider());
  }
}
