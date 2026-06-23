package hello.corebanking;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "hello.corebanking", annotationClass = Mapper.class)
public class CoreBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBankingApplication.class, args);
	}

}
