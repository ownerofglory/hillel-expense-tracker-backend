package ua.ithillel.expensetracker.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.ithillel.expensetracker.client.tool.AgentToolType;
import ua.ithillel.expensetracker.exception.ExpenseTrackerPersistingException;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.model.Expense;
import ua.ithillel.expensetracker.model.User;
import ua.ithillel.expensetracker.repo.ExpenseRepo;
import ua.ithillel.expensetracker.repo.UserRepo;
import ua.ithillel.expensetracker.tools.definition.AgentToolDef;
import ua.ithillel.expensetracker.tools.exception.ToolExecException;
import ua.ithillel.expensetracker.tools.types.DateTimeField;
import ua.ithillel.expensetracker.tools.types.LongField;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@Component
public class GetExpensesBetweenDateTool implements AgentToolDef {
     private GetExpensesBetweenDateToolSchema schema = new GetExpensesBetweenDateToolSchema("getExpensesBetweenDate", "Gives the expenses for the user between two given dates");

     private final ExpenseRepo expenseRepo;
     private final UserRepo userRepo;
     private final ExpenseMapper expenseMapper;
     private final ObjectMapper objectMapper;

     @Override
     public Object execute(String args) throws ToolExecException {
          try {
               GetExpensesBetweenDatesArgs arguments = objectMapper.readValue(args, GetExpensesBetweenDatesArgs.class);

               List<Expense> expensesBetweenDates = getExpensesBetweenDates(arguments);
               return expensesBetweenDates.stream()
                       .map(expenseMapper::expenseToExpenseDTO)
                       .toList();
          } catch (JsonProcessingException e) {
              throw new ToolExecException("Unable to process arguments: " + e.getMessage());
          } catch (ExpenseTrackerPersistingException e) {
              throw new ToolExecException("Error when executing tool: " + e.getMessage());
          }
     }

     private List<Expense> getExpensesBetweenDates(GetExpensesBetweenDatesArgs args) throws ExpenseTrackerPersistingException {
          Long userId = args.getUserId();
          User user = userRepo.find(userId).orElseThrow();

          return expenseRepo.findByUserBetweenDates(user, args.getFrom(), args.getTo());
     }

     @Data
     public static class GetExpensesBetweenDateToolSchema implements AgentToolType {
          private String name;
          private String description;

          public GetExpensesBetweenDateToolSchema(String name, String description) {
               this.name = name;
               this.description = description;
          }

          @JsonProperty(value = "type")
          private String type = "object";

          @JsonProperty(value = "properties")
          private GetExpensesBetweenDateToolParams properties = new GetExpensesBetweenDateToolParams();

          @Override
          public String getName() {
               return name;
          }

          @Override
          public String getDescription() {
               return description;
          }

          @Override
          public Object getParams() {
               return properties;
          }
     }

     @Data
     public static class GetExpensesBetweenDateToolParams {
          @JsonProperty("userId")
          private LongField userId = new LongField("ID of the user in the database");
          @JsonProperty("from")
          private DateTimeField from = new DateTimeField("Date and time from in format yyyy-MM-ddTHH:mm:ss");
          @JsonProperty("to")
          private DateTimeField to = new DateTimeField("Date and time to in format yyyy-MM-ddTHH:mm:ss");
     }

     @Data
     public static class GetExpensesBetweenDatesArgs {
          @JsonProperty("userId")
          private Long userId;
          @JsonProperty("from")
          private LocalDateTime from;
          @JsonProperty("to")
          private LocalDateTime to;
     }

}
