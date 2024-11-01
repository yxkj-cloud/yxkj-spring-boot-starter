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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBootApplication
public class YxkjStarterApplicationTest {
    @Autowired
    private YxkjDds yxkjDds;

    @DisplayName("测试Yxkj动态数据源自动装配")
    @Test
    void test_yxkj_dds_auto_config() {
        Assertions.assertNotNull(yxkjDds, "Yxkj动态数据源自动装配失败。");
    }

    @Test
    @DisplayName("测试加载系统参数")
    void test_system_properties_load() {
        Assertions.assertEquals("yxkj", System.getProperty("application.name"));
    }
}
