package ru.eustimenko.extension;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoadFromFileExtension implements ParameterResolver {

    public boolean supportsParameter(
            ParameterContext parameterContext, ExtensionContext extensionContext
    ) throws ParameterResolutionException {
        return parameterContext.getParameter().getAnnotation(LoadFile.class) != null;
    }

    @SneakyThrows
    public Object resolveParameter(
            ParameterContext parameterContext, ExtensionContext extensionContext
    ) throws ParameterResolutionException {

        LoadFile annotation = parameterContext.getParameter().getAnnotation(LoadFile.class);
        Class<?> targetClass = parameterContext.getTarget()
                .orElseThrow(() -> new IllegalStateException("Test class not found"))
                .getClass();

        return IOUtils.toString(targetClass.getResourceAsStream(annotation.value()), UTF_8);
    }
}
