package ua.ithillel.expensetracker.tools;

import ua.ithillel.expensetracker.client.tool.AgentToolType;

public abstract class AgentToolDef<Targ> {
    public abstract AgentToolType getSchema();

    public Object execute(ToolExecutorFunc<Targ> executorFunc, Targ argObject) {
        return executorFunc.apply(argObject);
    }
}
