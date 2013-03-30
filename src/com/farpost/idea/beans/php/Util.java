package com.farpost.idea.beans.php;

import com.google.common.base.Function;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nullable;

/**
 * User: zolotov
 * Date: 12/4/12
 */
public class Util {
  public static final Function<PsiNamedElement, LookupElement> TO_SETTER_OR_GETTER_LOOKUP = new Function<PsiNamedElement, LookupElement>() {
    @Override
    public LookupElement apply(PsiNamedElement method) {
      return LookupElementBuilder.create(method, setterOrGetterToField(method.getName())).withIcon(AllIcons.Nodes.Field);
    }
  };

  public static Pair<String, String> getNameAndNamespace(String fqn) {
    fqn = StringUtil.stripQuotesAroundValue(fqn);
    final int i = fqn.lastIndexOf('\\');
    String className = i >= 0 ? fqn.substring(i + 1) : fqn;
    String namespace = i >= 0 ? fqn.substring(0, i + 1) : "\\";
    return Pair.create(className, namespace);
  }

  public static String normalizeNamespace(final String namespace) {
    return StringUtil.startsWithChar(namespace, '\\')
           ? namespace
           : "\\" + namespace;
  }

  public static String setterOrGetterToField(@Nullable final String setterOrGetter) {
    return setterOrGetter != null && setterOrGetter.length() > 3
           ? StringUtil.decapitalize(setterOrGetter.substring(3))
           : "";
  }

  public static boolean isSetter(@Nullable String methodName) {
    if (methodName != null && methodName.startsWith("set")) {
      final String propertyName = methodName.substring(3);
      if (StringUtil.isCapitalized(propertyName)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isGetter(@Nullable String methodName) {
    if (methodName != null && methodName.startsWith("get")) {
      final String propertyName = methodName.substring(3);
      if (StringUtil.isCapitalized(propertyName)) {
        return true;
      }
    }
    return false;
  }
}
