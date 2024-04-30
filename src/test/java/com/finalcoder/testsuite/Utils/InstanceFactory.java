package com.finalcoder.testsuite.Utils;

import com.finalcoder.testsuite.Common.TestContext;
import io.cucumber.core.backend.ObjectFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.behaviors.Cached;
import org.picocontainer.lifecycle.DefaultLifecycleState;

@Log4j2
@NoArgsConstructor
public final class InstanceFactory implements ObjectFactory {
  private final Set<Class<?>> classes = new HashSet<>();
  private MutablePicoContainer pico;

  private static boolean isInstantiable(Class<?> clazz) {
    boolean isNonStaticInnerClass =
        !Modifier.isStatic(clazz.getModifiers()) && clazz.getEnclosingClass() != null;
    return Modifier.isPublic(clazz.getModifiers())
        && !Modifier.isAbstract(clazz.getModifiers())
        && !isNonStaticInnerClass;
  }

  @Override
  public void start() {
    if (pico == null) {
      pico = new PicoBuilder().withCaching().withLifecycle().build();
      for (Class<?> clazz : classes) {
        pico.addComponent(clazz);
      }
    } else {
      // we already get a pico container which is in "disposed" lifecycle,
      // so recycle it by defining a new lifecycle and removing all
      // instances
      pico.setLifecycleState(new DefaultLifecycleState());
      pico.getComponentAdapters()
          .forEach(
              cached -> {
                // remove all instances except TestContext
                if (!cached.getComponentImplementation().equals(TestContext.class)) {
                  ((Cached<?>) cached).flush();
                }
              });
    }
    pico.start();
  }

  @Override
  public void stop() {
    pico.stop();
    pico.dispose();
  }

  @Override
  public boolean addClass(Class<?> clazz) {
    if (isInstantiable(clazz) && classes.add(clazz)) {
      addConstructorDependencies(clazz);
    }
    return true;
  }

  @Override
  public <T> T getInstance(Class<T> type) {
    return pico.getComponent(type);
  }

  private void addConstructorDependencies(Class<?> clazz) {
    for (Constructor<?> constructor : clazz.getConstructors()) {
      for (Class<?> paramClazz : constructor.getParameterTypes()) {
        addClass(paramClazz);
      }
    }
  }
}
