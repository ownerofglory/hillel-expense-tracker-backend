package ua.ithillel.expensetracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ithillel.expensetracker.config.ServiceTestConfig;
import ua.ithillel.expensetracker.mapper.ExpenseMapper;
import ua.ithillel.expensetracker.util.Base64Converter;
import ua.ithillel.expensetracker.util.ImageConvertor;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class ServiceTestParent {
    @Autowired
    protected Base64Converter base64Converter;

    @Autowired
    protected ImageConvertor imageConvertor;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ExpenseMapper expenseMapper;
}
