package kr.go.spo.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@Configuration
public class LogbackFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        boolean denyConition = false;
        boolean acceptConition = false;

//        acceptConition = acceptConition || event.getLoggerName().startsWith("kr.go.spo");
        acceptConition= true;

        denyConition = denyConition || event.getMessage().contains(" ACT_");
        denyConition = denyConition || event.getLoggerName().contains("selectHistoricVariableInstance");
        denyConition = denyConition || event.getLoggerName().contains("ActiveMQSession");


        if (denyConition) {
            return FilterReply.DENY;
        }else{
            return FilterReply.ACCEPT;
        }
    }
}

