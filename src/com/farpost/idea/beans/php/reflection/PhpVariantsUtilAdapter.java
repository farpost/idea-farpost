package com.farpost.idea.beans.php.reflection;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiNamedElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: zolotov
 * Date: 12/4/12
 */
@SuppressWarnings("All")
public class PhpVariantsUtilAdapter {
  private static Class phpVariantsUtilClass;
  private static Method getLookupItemsForClassesMethod;
  private static Object classUsageContext;
  private static Class classUsageContextClass;

  static {
    try {
      phpVariantsUtilClass = Class.forName("com.jetbrains.php.completion.PhpVariantsUtil");
      classUsageContextClass = Class.forName("com.jetbrains.php.completion.ClassUsageContext");
      getLookupItemsForClassesMethod = phpVariantsUtilClass.getMethod("getLookupItemsForClasses", Collection.class, classUsageContextClass);
      classUsageContext = classUsageContextClass.getConstructor(Boolean.TYPE).newInstance(false);
      classUsageContextClass.getMethod("setInInstanceof", Boolean.TYPE).invoke(classUsageContext, true);
    } catch (NoSuchMethodException e) {
      ReflectionUtil.error(e);
    } catch (InstantiationException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (ClassNotFoundException e) {
      ReflectionUtil.error(e);
    }
  }


  public static List<LookupElement> getLookupItemsForClasses(Collection<PsiNamedElement> variants) {
    try {
      return (List<LookupElement>) getLookupItemsForClassesMethod.invoke(null, variants, classUsageContext);
    } catch (Exception e) {
      ReflectionUtil.error(e);
      return Collections.emptyList();
    }
  }
}
