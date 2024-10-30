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

package cloud.yxkj.spring.starter.env;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 系统参数加载器
 *
 * @author wangping
 * @since 1.0.0
 */
public class SystemPropertiesLoader {
    private static final String SYSTEM_PROPERTIES_FILE_NAME = "system-properties-filenames";

    private static final String DEFAULT_PROPERTIES_FILE_NAME = "application-system.properties,config/application-system.properties";

    public static void load() {
        String filenames = System.getProperty(SYSTEM_PROPERTIES_FILE_NAME, System.getenv(SYSTEM_PROPERTIES_FILE_NAME));
        filenames = filenames == null ? DEFAULT_PROPERTIES_FILE_NAME : filenames;

        for (String fileName : filenames.split(",")) {
            load(fileName);
        }
    }

    @SneakyThrows
    public static void load(String filename) {
        // 获取到所有文件
        Enumeration<URL> resources = SystemPropertiesLoader.class.getClassLoader().getResources(filename);
        List<URL> urlList = new ArrayList<>();
        while (resources.hasMoreElements()) {
            urlList.add(resources.nextElement());
        }

        // 排序，Jar包中的优先级低
        urlList.sort(Comparator.comparing(URL::getPath));

        // 读取属性
        for (URL url : urlList) {
            try (InputStream inputStream = url.openStream();
                 InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Properties properties = new Properties();
                properties.load(reader);
                System.getProperties().putAll(properties);
            }
        }
    }
}
