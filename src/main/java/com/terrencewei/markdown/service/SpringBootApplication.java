package com.terrencewei.markdown.service;


import org.springframework.boot.SpringApplication;

/**
 * Created by terrencewei on 3/13/17.
 *
 * @SpringBootApplication包含@Configuration、@EnableAutoConfiguration、@ComponentScan
 * 
 * 用于主类, 会自动扫描此文件下属的所有包中的其他Spring注解
 * 即:自动扫描SpringApplication.java所在的package com.terrencewei.*
 *
 * @Service用于标注业务层组件
 * @Controller用于标注控制层组件（如struts中的action）
 * @Repository用于标注数据访问组件，即DAO组件
 * @Component泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。
 * 
 * 只用于逻辑上分类,实际使用没区别
 *
 * 在需要注入的变量位置使用@Autowired即可
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

    public static void main(String[] args) {
        // main()方法启动了一个HTTP服务器程序，这个程序默认监听8080端口，并将HTTP请求转发给我们的应用来处理
        // 此处读取application.properties中设置的端口server.port=8081
        SpringApplication.run(SpringBootApplication.class, args);
        System.out.println("");
        System.out.println("");
        System.out.println("------------------Spring Boot startup finished!------------------");
        System.out.println("");
        System.out.println("");
    }

}
