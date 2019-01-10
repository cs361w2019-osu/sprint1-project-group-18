package conf;

import ninja.Filter;
import ninja.application.ApplicationFilters;

import java.util.List;

@SuppressWarnings("unused")
public class Filters implements ApplicationFilters {

    @Override
    public void addFilters(List<Class<? extends Filter>> filters) {
        // Add your application - wide filters here
        // filters.add(MyFilter.class);
    }
}
