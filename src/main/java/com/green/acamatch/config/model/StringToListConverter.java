package com.green.acamatch.config.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringToListConverter implements Converter<String, List<Integer>> {
    @Override
    public List<Integer> convert(String source) {
        // 문자열을 List로 변환하는 로직
        String[] items = source.replaceAll("[\\[\\]]", "").split(",");
        return Arrays.stream(items).map(Integer::parseInt).collect(Collectors.toList());
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}
