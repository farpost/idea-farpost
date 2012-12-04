package com.farpost.idea.beans.php;

import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;

/**
 * User: zolotov
 * Date: 12/4/12
 */
public class Util {
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
}
