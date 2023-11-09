package com.vipul.queuedcall.config;

import com.vipul.queuedcall.annotation.BatchedQueueCalled;
import com.vipul.queuedcall.annotation.QueueCalledController;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import org.reflections.Reflections;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TargetConfigTest {

    private TargetConfig targetConfig;

    @Test
    void testQueueCalledMethodsBean() {
        try(MockedConstruction<Reflections> mockReflections = Mockito.mockConstruction(Reflections.class,
                (mock, context) -> {
                    when(mock.getTypesAnnotatedWith(QueueCalledController.class)).thenReturn(Set.of(this.getClass()));
                })){
            this.targetConfig = new TargetConfig();
            ReflectionTestUtils.setField(
                    targetConfig, "rootPackage", "com.test");
            Map<String, Method> result = this.targetConfig.queueCalledMethods();
            Assert.assertEquals(5, result.size());
        }
    }

    @Test
    void testBatchedQueueCalledMethodsBean() {
        try(MockedConstruction<Reflections> mockReflections = Mockito.mockConstruction(Reflections.class,
                (mock, context) -> {
                    when(mock.getTypesAnnotatedWith(QueueCalledController.class)).thenReturn(Set.of(this.getClass()));
                })){
            this.targetConfig = new TargetConfig();
            ReflectionTestUtils.setField(
                    targetConfig, "rootPackage", "com.test");
            Map<String, Method> result = this.targetConfig.batchedQueueCalledMethods();
            Assert.assertEquals(1, result.size());
        }
    }

    @BatchedQueueCalled
    public void dummyTestMethod() {
        System.out.println("something");
    }
}
