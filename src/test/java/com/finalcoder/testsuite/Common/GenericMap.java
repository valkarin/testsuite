package com.finalcoder.testsuite.Common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@SuppressWarnings("unused")
public final class GenericMap {
  private final Map<String, Map<Class<?>, Object>> map;

  public GenericMap() {
    this.map = new HashMap<>();
  }

  /**
   * If the key doesn't exist, it will be created. If the key exists, it will be overridden.
   *
   * @param key Key
   * @param value Value
   * @param <T> Type of the value
   */
  public <T> void putOrUpdate(String key, T value) {
    Map<Class<?>, Object> valueMap = map.getOrDefault(key, new HashMap<>());
    valueMap.put(value.getClass(), value);
    map.put(key, valueMap);
  }

  /**
   * If the key doesn't exist, it will be created. If the key exists, this method does nothing.
   *
   * @param key Key
   * @param value Value
   * @param <T> Type of the value
   */
  public <T> void put(String key, T value) {
    if (containsKey(key)) return;
    putOrUpdate(key, value);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean containsKey(String key) {
    return map.containsKey(key);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(String key, Class<T> valueClass) {
    Map<Class<?>, Object> valueMap = map.get(key);
    if (valueMap == null) {
      return null;
    }
    return (T) valueMap.get(valueClass);
  }

  public String getString(String key) {
    return get(key, String.class);
  }

  public String getString(String key, String defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getString(key);
  }

  public BigDecimal getBigDecimal(String key) {
    return get(key, BigDecimal.class);
  }

  public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getBigDecimal(key);
  }

  public BigInteger getBigInteger(String key) {
    return get(key, BigInteger.class);
  }

  public BigInteger getBigInteger(String key, BigInteger defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getBigInteger(key);
  }

  public boolean getBoolean(String key) {
    return Boolean.TRUE.equals(get(key, Boolean.class));
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getBoolean(key);
  }

  public Boolean getBoolean(String key, Boolean defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return get(key, Boolean.class);
  }

  public int getInt(String key) {
    return get(key, Integer.class);
  }

  public int getInt(String key, int defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getInt(key);
  }

  public Integer getInteger(String key) {
    return get(key, Integer.class);
  }

  public Integer getInteger(String key, Integer defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getInteger(key);
  }

  public long getLong(String key) {
    return get(key, Long.class);
  }

  public long getLong(String key, long defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getLong(key);
  }

  public Long getLong(String key, Long defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getLong(key);
  }

  public float getFloat(String key) {
    return get(key, Float.class);
  }

  public float getFloat(String key, float defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getFloat(key);
  }

  public Float getFloat(String key, Float defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return get(key, Float.class);
  }

  public double getDouble(String key) {
    return get(key, Double.class);
  }

  public double getDouble(String key, double defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getDouble(key);
  }

  public Double getDouble(String key, Double defaultValue) {
    return get(key, Double.class);
  }

  @SuppressWarnings("unchecked")
  public <T> Collection<T> getList(String key, Class<T> valueType) {
    Map<Class<?>, Object> valueMap = map.get(key);
    if (valueMap == null) {
      return null;
    }
    Class<?> firstElement = (Class<?>) valueMap.keySet().toArray()[0];
    if (firstElement.equals(ArrayList.class)) return (ArrayList<T>) valueMap.get(ArrayList.class);
    else if (firstElement.equals(HashSet.class)) {
      return (HashSet<T>) valueMap.get(HashSet.class);
    }
    return null;
  }

  public <T> Collection<T> getList(String key, Class<T> valueType, Collection<T> defaultValue) {
    if (!containsKey(key)) return defaultValue;
    return getList(key, valueType);
  }

  @SuppressWarnings("unchecked")
  public <T> void addToList(String key, T value) {
    Collection<T> list = getList(key, (Class<T>) value.getClass(), new ArrayList<>());
    list.add(value);
    put(key, list);
  }

  public Set<String> getAllKeys() {
    return map.keySet();
  }

  public void clearAll() {
    map.clear();
  }

  public void removeKey(String key) {
    if (this.containsKey(key)) {
      map.remove(key);
    }
  }
}
