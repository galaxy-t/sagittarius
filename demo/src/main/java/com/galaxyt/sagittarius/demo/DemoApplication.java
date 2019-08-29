package com.galaxyt.sagittarius.demo;

import com.galaxyt.sagittarius.client.EnableSagittarius;
import com.galaxyt.sagittarius.client.config.ConfigOrdered;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *
 * @author zhouqi
 * @date 2019-06-14 17:01
 * @version v1.0.0
 * @Description
 *
 * Modification History:
 * Date                 Author          Version          Description
---------------------------------------------------------------------------------*
 * 2019-06-14 17:01     zhouqi          v1.0.0           Created
 *
 */
@SpringBootApplication(scanBasePackages = "com.galaxyt.sagittarius.demo")
@EnableSagittarius(applicationName = "demo", namespace = "dev", order = ConfigOrdered.REMOTE_FIRST)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


}
