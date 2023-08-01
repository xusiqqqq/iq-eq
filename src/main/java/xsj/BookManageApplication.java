package xsj;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@ServletComponentScan
//@MapperScan("com.mapper")
public class BookManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookManageApplication.class,args);
        log.info("项目启动成功");
    }
}
