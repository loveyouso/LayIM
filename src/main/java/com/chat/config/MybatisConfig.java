package com.chat.config;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author loveyouso
 * @create 2018-09-16 15:25
 **/
@Configuration
public class MybatisConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Bean
    public PageHelper pageHelper(){
        LOGGER.info("注册pagehelper分页插件");
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","true");
        //rowBoundsWithCount：默认值为false，
        // 该参数对使用 RowBounds 作为分页参数时有效。
        // 当该参数设置为true时，使用 RowBounds 分页会进行 count 查询。
        properties.setProperty("rowBoundsWithCount","true");
        //reasonable：分页合理化参数，默认值为false。当
        // 该参数设置为 true 时，pageNum<=0 时会查询第一页， pageNum>pages（超过总数时），会查询最后一页。
        // 默认false 时，直接根据参数进行查询。
        properties.setProperty("reasonable","true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
