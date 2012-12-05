package com.farpost.idea.beans.php.reflection;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: zolotov
 * Date: 12/6/12
 */
@SuppressWarnings("All")
public class PhpImplementationsProviderAdapter {
  private static Object phpImplementationsProvider;
  private static Class phpImplementationsProviderClass;
  private static Method getElementsMethod;

  static {
    try {
      phpImplementationsProviderClass = Class.forName("com.jetbrains.php.uml.providers.PhpImplementationsProvider");
      phpImplementationsProvider = phpImplementationsProviderClass.newInstance();
      getElementsMethod = phpImplementationsProviderClass.getMethod("getElements", PhpClassAdapter.phpClassClass, Project.class);
    } catch (ClassNotFoundException e) {
      ReflectionUtil.error(e);
    } catch (NoSuchMethodException e) {
      ReflectionUtil.error(e);
    } catch (InstantiationException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
  }

  public static PsiNamedElement[] getAllImplementations(PsiElement phpClass) {
    try {
      return (PsiNamedElement[]) getElementsMethod.invoke(phpImplementationsProvider, phpClass, phpClass.getProject());
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    }
    return PsiNamedElement.EMPTY_ARRAY;
  }
}
