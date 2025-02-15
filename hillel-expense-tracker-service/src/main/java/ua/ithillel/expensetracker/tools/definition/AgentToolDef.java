package ua.ithillel.expensetracker.tools.definition;

import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.tools.exception.ToolExecException;

public interface AgentToolDef {
    AgentToolType getSchema();
    Object execute(String args) throws ToolExecException;
}
