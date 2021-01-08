package ru.itis.services;

import java.io.Writer;
import java.util.Map;

public interface TemplateResolver {
    String process(String name, Map<String, String> root);
    void process(String name, Map<String, String> root, Writer writer);
}
