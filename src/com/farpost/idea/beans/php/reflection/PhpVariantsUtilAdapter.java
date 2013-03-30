package com.farpost.idea.beans.php.reflection;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiNamedElement;
import com.jetbrains.php.completion.ClassUsageContext;

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

  static {
    try {
      phpVariantsUtilClass = Class.forName("com.jetbrains.php.completion.PhpVariantsUtil");
      getLookupItemsForClassesMethod =
        phpVariantsUtilClass.getMethod("getLookupItemsForClasses", Collection.class, ClassUsageContext.class);
    }
    catch (NoSuchMethodException e) {
      ReflectionUtil.error(e);
    }
    catch (ClassNotFoundException e) {
      ReflectionUtil.error(e);
    }
  }


  public static List<LookupElement> getLookupItemsForClasses(Collection<PsiNamedElement> variants, Object classUsageContext) {
    try {
      return (List<LookupElement>)getLookupItemsForClassesMethod.invoke(null, variants, classUsageContext);
    }
    catch (Exception e) {
      ReflectionUtil.error(e);
      return Collections.emptyList();
    }
  }
}
