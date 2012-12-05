package com.farpost.idea.beans.php.reflection;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * User: zolotov
 * Date: 12/4/12
 */
@SuppressWarnings("All")
public class PhpIndexAdapter {
  public static Class phpIndexClass;

  private static Method getIndexInstanceMethod;
  private static Method getClassesByFQNMethod;
  private static Method getAllClassNamesMethod;
  private static Method getClassesByNameMethod;
  private static Method filterByNamespaceMethod;
  private static Method getInterfacesByFQNMethod;

  private static final PhpIndexAdapter INVALID_ADAPTER = new PhpIndexAdapter(null);
  private static final LoadingCache<Project, PhpIndexAdapter> ourCache =
          CacheBuilder.newBuilder().initialCapacity(1).build(CacheLoader.from(new Function<Project, PhpIndexAdapter>() {
            @Override
            public PhpIndexAdapter apply(Project project) {
              try {
                return new PhpIndexAdapter(getIndexInstanceMethod.invoke(null, project));
              } catch (InvocationTargetException e) {
                ReflectionUtil.error(e);
              } catch (IllegalAccessException e) {
                ReflectionUtil.error(e);
              }
              return INVALID_ADAPTER;
            }
          }));

  static {
    try {
      phpIndexClass = Class.forName("com.jetbrains.php.PhpIndex");
      getIndexInstanceMethod = phpIndexClass.getMethod("getInstance", Project.class);
      getClassesByFQNMethod = phpIndexClass.getMethod("getClassesByFQN", String.class);
      getInterfacesByFQNMethod = phpIndexClass.getMethod("getInterfacesByFQN", String.class);
      getAllClassNamesMethod = phpIndexClass.getMethod("getAllClassNames", PrefixMatcher.class);
      getClassesByNameMethod = phpIndexClass.getMethod("getClassesByName", String.class);
      filterByNamespaceMethod = phpIndexClass.getMethod("filterByNamespace", Collection.class, String.class);
    } catch (ClassNotFoundException e) {
      ReflectionUtil.error(e);
    } catch (NoSuchMethodException e) {
      ReflectionUtil.error(e);
    }
  }

  @Nullable
  private final Object phpIndex;

  private PhpIndexAdapter(@Nullable Object phpIndex) {
    this.phpIndex = phpIndex;
  }

  @NotNull
  public static PhpIndexAdapter getInstance(Project project) {
    try {
      return ourCache.get(project);
    } catch (ExecutionException e) {
      ReflectionUtil.error(e);
      return INVALID_ADAPTER;
    }
  }

  public Collection<PsiNamedElement> getClassesByFQN(String fqn) {
    try {
      return (Collection<PsiNamedElement>) getClassesByFQNMethod.invoke(phpIndex, fqn);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
    return Collections.emptyList();
  }

  public Collection<String> getAllClassNames(PrefixMatcher prefixMatcher) {
    try {
      return (Collection<String>) getAllClassNamesMethod.invoke(phpIndex, prefixMatcher);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
    return Collections.emptyList();
  }

  public Collection<PsiNamedElement> getClassesByName(String className) {
    try {
      return (Collection<PsiNamedElement>) getClassesByNameMethod.invoke(phpIndex, className);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
    return Collections.emptyList();
  }

  public Collection<PsiNamedElement> filterByNamespace(Collection<? extends PsiElement> classesByName, String namespace) {
    try {
      return (Collection<PsiNamedElement>) filterByNamespaceMethod.invoke(null, classesByName, namespace);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
    return Collections.emptyList();
  }

  public Object getIndex() {
    return phpIndex;
  }

  public Collection<PsiNamedElement> getInterfacesByFQN(String fqn) {
    try {
      return (Collection<PsiNamedElement>) getInterfacesByFQNMethod.invoke(phpIndex, fqn);
    } catch (InvocationTargetException e) {
      ReflectionUtil.error(e);
    } catch (IllegalAccessException e) {
      ReflectionUtil.error(e);
    }
    return Collections.emptyList();
  }
}
