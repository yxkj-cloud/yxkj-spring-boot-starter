/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package cloud.yxkj.spring.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Optional;

/**
 * 多实例，当存在多个指定的实例的时候，则匹配
 *
 * @author : wping
 * @since : 2020/4/16 15:07
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional({ConditionalOnMultipleCandidate.OnBeanCondition.class})
public @interface ConditionalOnMultipleCandidate {
    /**
     * The class type of bean that should be checked. The condition matches if a bean of
     * the class specified is contained in the {@link BeanFactory} and a primary candidate
     * exists in case of multiple instances.
     * <p>
     * This attribute may <strong>not</strong> be used in conjunction with
     * {@link #type()}, but it may be used instead of {@link #type()}.
     *
     * @return the class type of the bean to check
     */
    Class<?> value() default Object.class;

    /**
     * The class type name of bean that should be checked. The condition matches if a bean
     * of the class specified is contained in the {@link BeanFactory} and a primary
     * candidate exists in case of multiple instances.
     * <p>
     * This attribute may <strong>not</strong> be used in conjunction with
     * {@link #value()}, but it may be used instead of {@link #value()}.
     *
     * @return the class type name of the bean to check
     */
    String type() default "";

    /**
     * Strategy to decide if the application context hierarchy (parent contexts) should be
     * considered.
     *
     * @return the search strategy
     */
    SearchStrategy search() default SearchStrategy.ALL;


    @Slf4j
    class OnBeanCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            if (!metadata.isAnnotated("cloud.yxkj.spring.starter.ConditionalOnMultipleCandidate")) {
                return false;
            }

            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnMultipleCandidate.class.getName());
            if (attributes == null) {
                return false;
            }
            Class value = Optional.ofNullable((Class) attributes.get("value"))
                .orElseGet(() -> forName((String) attributes.get("type")));
            if (value == null) {
                return false;
            }
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            return beanFactory != null && beanFactory.getBeanNamesForType(value).length > 1;
        }

        Class<?> forName(String className) {
            if (className == null || className.trim().isEmpty()) {
                return null;
            }
            try {
                return ClassUtils.forName(className.trim(), this.getClass().getClassLoader());
            } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                log.warn("Class load failed. {}", className, ex);
                return null;
            }
        }
    }
}
