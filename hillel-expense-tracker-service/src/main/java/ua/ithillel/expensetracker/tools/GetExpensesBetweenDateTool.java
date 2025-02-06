package ua.ithillel.expensetracker.tools;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetExpensesBetweenDateTool extends AgentToolDef<GetExpensesBetweenDatesArgs> {
     private GetExpensesBetweenDateToolSchema schema;
}
