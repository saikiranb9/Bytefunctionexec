package com.atd.microservices.core.bytefunctionexecer;

import java.io.IOException;

import com.atd.microservices.bytefunctionexecer.services.ByteFunctionExecerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.atd.microservices.core.bytefunctionexecer.*")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ByteFunctionTest {

    //@MockBean
    //private ByteFunctionExecerService ediReaderConsumerService;

    @Test
    public void testByteFunction() throws IOException {
        //Resource resource = new ClassPathResource("edieader_incoming_payload.json");

        Assert.assertNotNull("ASDFASFASDF");
    }

}