package ua.ithillel.expensetracker.config;

import org.springframework.beans.factory.FactoryBean;
import ua.ithillel.expensetracker.client.ExternalCategoryClient;
import ua.ithillel.expensetracker.client.impl.ExternalCategoryClientDefault;
import ua.ithillel.expensetracker.client.impl.ExternalCategoryClientUSA;

public class ExternalCategoryClientFactory implements FactoryBean<ExternalCategoryClient> {
    @Override
    public ExternalCategoryClient getObject() throws Exception {
        String region = System.getenv("REGION");
        if (region == null || region.isEmpty() || region.equals("Europe")) {
            return new ExternalCategoryClientDefault();
        }

        return new ExternalCategoryClientUSA();
    }

    @Override
    public Class<?> getObjectType() {
        String region = System.getenv("REGION");
        if (region == null || region.isEmpty() || region.equals("Europe")) {
            return ExternalCategoryClientDefault.class;
        }

        return ExternalCategoryClientUSA.class;
    }
}
