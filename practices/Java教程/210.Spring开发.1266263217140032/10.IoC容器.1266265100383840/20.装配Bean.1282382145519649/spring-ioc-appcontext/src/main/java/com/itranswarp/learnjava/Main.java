package com.itranswarp.learnjava;

import com.itranswarp.learnjava.service.User;
import com.itranswarp.learnjava.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
//		最后一步，我们需要创建一个Spring的IoC容器实例，然后加载配置文件，让Spring容器为我们创建并装配好配置文件中指定的所有Bean
		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
//		也可以使用BeanFactory，区别在于BeanFactory是按需创建，而ApplicationContext是一次性全部创建，同时提供了一些额外的功能
//		其实后者就是继承了前者的接口而来的
// 		一般情况下推荐使用后者，因为在初始化时就报错比服务正常运行再报错时去排查要好
//		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("application.xml"));

		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		userService.register("stack@example.com","123","stack");
		System.out.println(user.getName());
	}
}
