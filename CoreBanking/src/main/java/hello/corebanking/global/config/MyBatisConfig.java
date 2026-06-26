package hello.corebanking.global.config;

import hello.corebanking.domain.loan.entity.LoanStatus;
import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.repayment.entity.DelinquencyReason;
import hello.corebanking.domain.repayment.entity.DelinquencyStatus;
import hello.corebanking.domain.repayment.entity.RepaymentScheduleStatus;
import hello.corebanking.global.config.typehandler.DelinquencyReasonTypeHandler;
import hello.corebanking.global.config.typehandler.DelinquencyStatusTypeHandler;
import hello.corebanking.global.config.typehandler.InterestTypeTypeHandler;
import hello.corebanking.global.config.typehandler.LoanStatusTypeHandler;
import hello.corebanking.global.config.typehandler.RepaymentMethodTypeHandler;
import hello.corebanking.global.config.typehandler.RepaymentScheduleStatusTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml")
        );

        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setMapUnderscoreToCamelCase(true);
        config.getTypeHandlerRegistry().register(InterestType.class, new InterestTypeTypeHandler());
        config.getTypeHandlerRegistry().register(RepaymentMethod.class, new RepaymentMethodTypeHandler());
        config.getTypeHandlerRegistry().register(LoanStatus.class, new LoanStatusTypeHandler());
        config.getTypeHandlerRegistry().register(RepaymentScheduleStatus.class, new RepaymentScheduleStatusTypeHandler());
        config.getTypeHandlerRegistry().register(DelinquencyStatus.class, new DelinquencyStatusTypeHandler());
        config.getTypeHandlerRegistry().register(DelinquencyReason.class, new DelinquencyReasonTypeHandler());
        factoryBean.setConfiguration(config);

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
