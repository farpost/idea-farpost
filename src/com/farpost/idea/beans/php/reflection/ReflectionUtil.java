package com.farpost.idea.beans.php.reflection;

import com.intellij.openapi.diagnostic.Logger;

/**
 * User: zolotov
 * Date: 12/5/12
 */
public class ReflectionUtil {
  private static final Logger LOG = Logger.getInstance(ReflectionUtil.class);

  public static void error(Exception e) {
    LOG.warn("Reflection error", e);
  }
}
