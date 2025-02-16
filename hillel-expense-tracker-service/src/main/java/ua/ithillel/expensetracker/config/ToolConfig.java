package ua.ithillel.expensetracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.ithillel.expensetracker.tools.GetExpensesBetweenDateTool;
import ua.ithillel.expensetracker.tools.definition.AgentToolDef;

import java.util.Map;

@Configuration
public class ToolConfig {
    @Autowired
    private GetExpensesBetweenDateTool getExpensesBetweenDateTool;

    @Bean(name = "agentTools")
    public Map<String, AgentToolDef> agentTools() {
        return Map.of(getExpensesBetweenDateTool.getSchema().getName(), getExpensesBetweenDateTool);
    }
}
