package com.farpost.idea.beans;

import com.farpost.idea.beans.php.BeanClassReferenceProvider;
import com.farpost.idea.beans.php.BeanPropertyReferenceProvider;
import com.farpost.idea.beans.php.BeanToolkitMethodReferenceProvider;
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
    registrar.registerReferenceProvider(Patterns.beanPropertyPattern(), new BeanPropertyReferenceProvider());
    registrar.registerReferenceProvider(Patterns.toolkitMethodPattern(), new BeanToolkitMethodReferenceProvider());
  }
}
