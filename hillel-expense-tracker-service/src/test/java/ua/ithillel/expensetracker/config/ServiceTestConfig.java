package ua.ithillel.expensetracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.tools.definition.AgentToolDef;
import ua.ithillel.expensetracker.tools.exception.ToolExecException;
import ua.ithillel.expensetracker.util.Base64Converter;
import ua.ithillel.expensetracker.util.ImageConvertor;

import java.util.Map;

@Configuration
public class ServiceTestConfig {
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Base64Converter base64Converter() {
        return new Base64Converter();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ExpenseMapper expenseMapper() {
        return ExpenseMapper.INSTANCE;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ImageConvertor imageConvertor() {
        return new ImageConvertor();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        return objectMapper;
    }

    @Bean(name = "agentTools")
    public Map<String, AgentToolDef> agentTools() {
        return Map.of("toolname", new AgentToolDef() {
            @Override
            public AgentToolType getSchema() {
                return new AgentToolType() {
                    @Override
                    public String getName() {
                        return "";
                    }

                    @Override
                    public String getDescription() {
                        return "";
                    }

                    @Override
                    public Object getParams() {
                        return null;
                    }
                };
            }

            @Override
            public Object execute(String args) throws ToolExecException {
                return null;
            }
        });
    }
}
