package com.scaffold.curd_generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CrudGeneratorCommand implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java -jar crud-generator.jar <Checker> <EntityName> <PackageName> <fields>");
            System.out.println("Example: java -jar crud-generator.jar true Product com.example \"name:String,price:Double\"");
            return;
        }

        boolean checker = Boolean.parseBoolean(args[0]);
        String entityName = args[1];
        String packageName = args[2];
        String fieldsInput = args[3];

        List<Map<String, String>> fields = new ArrayList<>();
        for (String field : fieldsInput.split(",")) {
            String[] parts = field.split(":");
            if (parts.length == 2) {
                Map<String, String> fieldMap = new HashMap<>();
                fieldMap.put("name", parts[0].trim());
                fieldMap.put("type", parts[1].trim());
                fields.add(fieldMap);
            }
        }

        Map<String, Object> context = new HashMap<>();
        context.put("packageName", packageName);
        context.put("EntityName", entityName);
        context.put("tableName", entityName.toLowerCase() + "s");
        context.put("fields", fields);

        // Target directories in a Spring Boot project
        String basePath = "src/main/java/" + packageName.replace(".", "/");
        if (checker) {
            generateFile("templates/entityTemp.mustache", basePath + "/model/temp/" + entityName + "Temp.java", context);
            generateFile("templates/repositoryTemp.mustache", basePath + "/repository/" + entityName + "TempRepository.java", context);
        }
        generateFile("templates/entityDTO.mustache", basePath + "/dto/" + entityName + "DTO.java", context);
        generateFile("templates/entity.mustache", basePath + "/model/" + entityName + ".java", context);
        generateFile("templates/repository.mustache", basePath + "/repository/" + entityName + "Repository.java", context);
        generateFile("templates/service.mustache", basePath + "/service/" + entityName + "Service.java", context);
        generateFile("templates/serviceImpl.mustache", basePath + "/service/impl/" + entityName + "ServiceImpl.java", context);
        generateFile("templates/controller.mustache", basePath + "/controller/" + entityName + "Controller.java", context);
    }

    private void generateFile(String templateName, String outputFile, Map<String, Object> context) throws Exception {
        MustacheFactory mf = new DefaultMustacheFactory(resource -> {
            try {
                return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resource));
            } catch (Exception e) {
                throw new MustacheNotFoundException(resource, e);
            }
        });

        Mustache mustache = mf.compile(templateName);
        StringWriter writer = new StringWriter();
        mustache.execute(writer, context).flush();

        Path path = Path.of(outputFile);
        Files.createDirectories(path.getParent()); // Create directories if not exist
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(writer.toString());
        }
        System.out.println("Generated: " + outputFile);
    }

}
