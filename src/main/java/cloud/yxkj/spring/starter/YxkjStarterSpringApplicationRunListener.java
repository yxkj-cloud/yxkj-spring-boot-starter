package cloud.yxkj.spring.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Objects;

/**
 * YxkjStarterSpringApplicationRunListener
 * Created on 2024/8/23
 *
 * @author wangping
 * @since 2024-08-23
 */
@Slf4j
public class YxkjStarterSpringApplicationRunListener implements SpringApplicationRunListener {
    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        log.info("=======================================================");
        log.info("Yxkj Application 已启动完成，耗时: {} 秒", timeTaken.getSeconds());
        log.info("=======================================================");
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        // 检查是否属于服务器环境
        boolean isServerEnv = context.containsBean("serverProperties");
        if (!isServerEnv) {
            return;
        }

        // 服务器环境打印 访问地址
        ServerProperties serverProperties = context.getBean(ServerProperties.class);
        String contextPath = Objects.toString(serverProperties.getServlet().getContextPath(), "");
        String port = Objects.toString(serverProperties.getPort(), "8080");
        String url = MessageFormat.format("http://localhost.yxkj.cloud:{0}{1}", port, contextPath);
        log.info("Yxkj Application 启动完成，访问：{}", url);
    }
}
