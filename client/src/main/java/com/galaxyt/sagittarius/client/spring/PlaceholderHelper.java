package com.galaxyt.sagittarius.client.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 占位符工具类
 * @author zhouqi
 * @date 2019-07-03 16:00
 * @version v1.0.0
 * @Description
 * 高手写的，不是我写的，我可没这水平
 * 原始代码就是一个普通的类，通过 google 的工具类写了一个 Spring Bean 注射器提供单例的支持
 * 不太想这样做所以自己实现了枚举懒汉模式
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-07-03 16:00     zhouqi          v1.0.0           主要用于操作各种占位符，如 ${}  #{${}} 等等，第一版本仅考虑 ${a} 这种情况
 *
 */
public enum PlaceholderHelper {


  INSTANCE;

  private static final String PLACEHOLDER_PREFIX = "${";
  private static final String PLACEHOLDER_SUFFIX = "}";
  private static final String VALUE_SEPARATOR = ":";
  private static final String SIMPLE_PLACEHOLDER_PREFIX = "{";
  private static final String EXPRESSION_PREFIX = "#{";
  private static final String EXPRESSION_SUFFIX = "}";


  /**
   * 提取占位符中的 key 若占位符规则不匹配则会直接抛出错误或者返回null
   * @param propertyString
   * @return
   */
  public String extractPlaceholderKey(String propertyString) {

    if (!isNormalizedPlaceholder(propertyString)) {
      return null;
    }

    propertyString = propertyString.replace(" ", "");

    return propertyString.substring(2, propertyString.length() - 1);
  }




  /**
   * Resolve placeholder property values, e.g.
   * <br />
   * <br />
   * "${somePropertyValue}" -> "the actual property value"
   */
  public Object resolvePropertyValue(ConfigurableBeanFactory beanFactory, String beanName, String placeholder) {
    // resolve string value
    String strVal = beanFactory.resolveEmbeddedValue(placeholder);

    BeanDefinition bd = (beanFactory.containsBean(beanName) ? beanFactory
        .getMergedBeanDefinition(beanName) : null);

    // resolve expressions like "#{systemProperties.myProp}"
    return evaluateBeanDefinitionString(beanFactory, strVal, bd);
  }

  private Object evaluateBeanDefinitionString(ConfigurableBeanFactory beanFactory, String value,
                                              BeanDefinition beanDefinition) {
    if (beanFactory.getBeanExpressionResolver() == null) {
      return value;
    }
    Scope scope = (beanDefinition != null ? beanFactory
        .getRegisteredScope(beanDefinition.getScope()) : null);
    return beanFactory.getBeanExpressionResolver()
        .evaluate(value, new BeanExpressionContext(beanFactory, scope));
  }
  /**
   * 从占位符中提取密钥
   * @param propertyString
   * @return
   *    例：
   *      ${some.key} => "some.key"
   *      ${some.key:${some.other.key:100}} => "some.key", "some.other.key"
   *      ${${some.key}} => "some.key"
   *      ${${some.key:other.key}} => "some.key"
   *      ${${some.key}:${another.key}} => "some.key", "another.key"
   *      ${new java.text.SimpleDateFormat('${some.key}').parse('${another.key}')} => "some.key", "another.key"
   *
   */
  public Set<String> extractPlaceholderKeys(String propertyString) {
    Set<String> placeholderKeys = new HashSet<>();

    if (!isNormalizedPlaceholder(propertyString) && !isExpressionWithPlaceholder(propertyString)) {
      return placeholderKeys;
    }

    Stack<String> stack = new Stack<>();
    stack.push(propertyString);

    while (!stack.isEmpty()) {
      String strVal = stack.pop();
      int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
      if (startIndex == -1) {
        placeholderKeys.add(strVal);
        continue;
      }
      int endIndex = findPlaceholderEndIndex(strVal, startIndex);
      if (endIndex == -1) {
        // invalid placeholder?
        continue;
      }

      String placeholderCandidate = strVal.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);

      // ${some.key:other.key}
      if (placeholderCandidate.startsWith(PLACEHOLDER_PREFIX)) {
        stack.push(placeholderCandidate);
      } else {
        // some.key:${some.other.key:100}
        int separatorIndex = placeholderCandidate.indexOf(VALUE_SEPARATOR);

        if (separatorIndex == -1) {
          stack.push(placeholderCandidate);
        } else {
          stack.push(placeholderCandidate.substring(0, separatorIndex));
          String defaultValuePart =
              normalizeToPlaceholder(placeholderCandidate.substring(separatorIndex + VALUE_SEPARATOR.length()));
          if (!StringUtils.isEmpty(defaultValuePart)) {
            stack.push(defaultValuePart);
          }
        }
      }

      // has remaining part, e.g. ${a}.${b}
      if (endIndex + PLACEHOLDER_SUFFIX.length() < strVal.length() - 1) {
        String remainingPart = normalizeToPlaceholder(strVal.substring(endIndex + PLACEHOLDER_SUFFIX.length()));
        if (!StringUtils.isEmpty(remainingPart)) {
          stack.push(remainingPart);
        }
      }
    }

    return placeholderKeys;
  }

  /**
   * 检查是否为规范化的占位符
   * @param propertyString
   * @return
   *  若要返回 true 占位符必须以 "${" 开始，以 "}" 结束
   *  例：
   *     ${a} => true
   *     ${com.spring.a} => true
   *     #{a} => false
   *     #{com.spring.a} => falwe
   */
  private boolean isNormalizedPlaceholder(String propertyString) {
    return propertyString.startsWith(PLACEHOLDER_PREFIX) && propertyString.endsWith(PLACEHOLDER_SUFFIX);
  }

  /**
   * 检查是否为带占位符的表达式
   * @param propertyString
   * @return
   *  若要返回 true 占位符必须以 "#{" 开始，以 "}" 结束，中间需要包含 "${"
   *  例：
   *    #{${a}} => true
   *    #{${com.spring.a}} = true
   *    ${a} => false
   *    ${com.spring.a} => false
   */
  private boolean isExpressionWithPlaceholder(String propertyString) {
    return propertyString.startsWith(EXPRESSION_PREFIX) && propertyString.endsWith(EXPRESSION_SUFFIX)
        && propertyString.contains(PLACEHOLDER_PREFIX);
  }

  private String normalizeToPlaceholder(String strVal) {
    int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
    if (startIndex == -1) {
      return null;
    }
    int endIndex = strVal.lastIndexOf(PLACEHOLDER_SUFFIX);
    if (endIndex == -1) {
      return null;
    }

    return strVal.substring(startIndex, endIndex + PLACEHOLDER_SUFFIX.length());
  }

  private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
    int index = startIndex + PLACEHOLDER_PREFIX.length();
    int withinNestedPlaceholder = 0;
    while (index < buf.length()) {
      if (StringUtils.substringMatch(buf, index, PLACEHOLDER_SUFFIX)) {
        if (withinNestedPlaceholder > 0) {
          withinNestedPlaceholder--;
          index = index + PLACEHOLDER_SUFFIX.length();
        } else {
          return index;
        }
      } else if (StringUtils.substringMatch(buf, index, SIMPLE_PLACEHOLDER_PREFIX)) {
        withinNestedPlaceholder++;
        index = index + SIMPLE_PLACEHOLDER_PREFIX.length();
      } else {
        index++;
      }
    }
    return -1;
  }
}
