package com.vipul.queuedcall.core;

import com.vipul.queuedcall.annotation.QueuedCallApi;
import com.vipul.queuedcall.model.QueuedCallRequest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reflections.Reflections;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class QueuedCallApiInitiatorTest {

    private static String METHOD_NAME = "testMethod";

    @Mock
    private QueuedCallSender queuedCallSender;
    @Mock
    private GenericApplicationContext applicationContext;

    @Captor
    private ArgumentCaptor<QueuedCallRequest> queuedCallRequestArgumentCaptor;

    private Method method;
    private QueuedCallApiInitiator queuedCallApiInitiator;

    @BeforeEach
    void setUp() {
        this.queuedCallApiInitiator = new QueuedCallApiInitiator(queuedCallSender, applicationContext);
    }

    @Test
    public void testSendRequest() throws NoSuchMethodException {
        method = this.getClass().getDeclaredMethod("testMethod");


        CompletableFuture<Object> resultFuture =
                this.queuedCallApiInitiator.sendRequest(method, new Object[0]);

        verify(queuedCallSender, times(1)).send(queuedCallRequestArgumentCaptor.capture());

        assertThat(queuedCallRequestArgumentCaptor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(
                        QueuedCallRequest.builder()
                            .id("")
                            .type(QueuedCallType.REQUEST)
                            .name(METHOD_NAME)
                            .paramTypes(method.getParameterTypes())
                            .args(new Class[]{})
                            .build()
                );

        Assert.assertNotNull(resultFuture);
    }

    @Test
    public void testRegisterQueuedApis() {
        try(MockedConstruction<Reflections> mockReflections = Mockito.mockConstruction(Reflections.class,
                (mock, context) -> {
            when(mock.getTypesAnnotatedWith(QueuedCallApi.class)).thenReturn(Set.of(QueuedCallSender.class));
        })){
            ReflectionTestUtils.setField(
                    queuedCallApiInitiator, "rootPackage", "com.test");
            this.queuedCallApiInitiator.registerQueuedApis();
            verify(applicationContext, times(1)).registerBean(anyString(), any(), any(), any());
        }

    }

    public void testMethod() {
        System.out.println("Hello");
    }
}
