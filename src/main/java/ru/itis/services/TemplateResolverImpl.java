package ru.itis.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class TemplateResolverImpl implements TemplateResolver {
    private Configuration configuration;

    public TemplateResolverImpl() {
        configuration = freeMarkerConfiguration();
    }

    @Override
    public void process(String name, Map<String, String> root, Writer writer) {
        try {
            Template template = configuration.getTemplate(name);
            template.process(root, writer);
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String process(String name, Map<String, String> root) {
        try {
            Template template = configuration.getTemplate(name);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, root);
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }
    public freemarker.template.Configuration freeMarkerConfiguration() {
        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_28);
        freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/templates");
        return freemarkerConfiguration;
    }
}
