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

package cloud.yxkj.spring.starter.dds;

import cloud.yxkj.dds.YxkjDds;
import cloud.yxkj.spring.starter.ConditionalOnMultipleCandidate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源自动配置类
 *
 * @author wping
 * @since 2024/10/14 15:08
 */
@EnableConfigurationProperties(YxkjDdsProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "yxkj.enable-dds", havingValue = "true", matchIfMissing = true)
public class YxkjDdsAutoConfiguration {
    private final YxkjDdsProperties yxkjDdsProperties;

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    YxkjDds yxkjDdsWithNoDataSource() {
        Map<Object, DataSource> dataSourceMap = new LinkedHashMap<>();
        yxkjDdsProperties.getDds().forEach((key, val) -> {
            DataSource dataSource = val.initializeDataSourceBuilder().build();
            dataSourceMap.put(key, dataSource);
        });
        return setActiveAndReturn(new YxkjDds(dataSourceMap));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnMultipleCandidate(DataSource.class)
    @Primary
    YxkjDds yxkjDdsWithPrimary(Map<String, DataSource> dataSourceMap) {
        return setActiveAndReturn(new YxkjDds(new HashMap<>(dataSourceMap)));
    }

    private YxkjDds setActiveAndReturn(YxkjDds yxkjDds) {
        List<String> defaultDs = yxkjDdsProperties.getDefaultDs();
        defaultDs = defaultDs.isEmpty() ? new ArrayList<>(yxkjDdsProperties.getDds().keySet()) : defaultDs;
        yxkjDds.setActive(defaultDs.toArray(new Object[0]));
        return yxkjDds;
    }
}
