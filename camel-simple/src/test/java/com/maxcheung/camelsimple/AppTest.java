package com.maxcheung.camelsimple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;

@RunWith(MockitoJUnitRunner.class)
public class AppTest {
    
	  private static final String ACTIVE_PROFILE_ARG = "spring.profiles.active=local";

    /**
     * Test object.
     */
    @InjectMocks
    private Application app;

    /**
     * {@link Spy} {@link SpringApplicationBuilder}.
     */
    @Spy
    private SpringApplicationBuilder builder = new SpringApplicationBuilder(Application.class);

    /**
     * Ensures the source of the configured application is the {@link App}.
     */
    @Test
    public void shouldRunAsApplication() {
        String[] properties = new String[1];
        properties[0] = ACTIVE_PROFILE_ARG;
		app.main(properties);
    }




}
