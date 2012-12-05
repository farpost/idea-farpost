package com.farpost.idea.beans.php.reflection;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

/**
 * User: zolotov
 * Date: 12/5/12
 */
@SuppressWarnings("All")
public class PhpClassAdapter {
  public static Class phpClassClass;
  private static Method getMethodsMethod;

  static {
    try {
      phpClassClass = Class.forName("com.jetbrains.php.lang.psi.elements.PhpClass");
      getMethodsMethod = phpClassClass.getMethod("getMethods");
    } catch (ClassNotFoundException e) {
      ReflectionUtil.error(e);
    } catch (NoSuchMethodException e) {
      ReflectionUtil.error(e);
    }
  }

  public static Collection<PsiNamedElement> getAllMethods(@NotNull PsiElement classObject) {
    try {
      return (Collection<PsiNamedElement>) getMethodsMethod.invoke(classObject);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    }
    return Collections.emptyList();
  }

}
