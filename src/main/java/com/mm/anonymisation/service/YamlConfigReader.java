package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import com.mm.anonymisation.service.generator.FieldGenerator;
import com.mm.anonymisation.service.processor.RowPostProcessor;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

@RequiredArgsConstructor
public class YamlConfigReader implements ConfigReader {

  private final SchemaRepository repository;

  @Override
  public List<Table> read(InputStream inputStream) {
    var yaml = new Yaml();
    yaml.setBeanAccess(BeanAccess.FIELD);
    List<Map<String, Map<String, Object>>> tablesConfig = yaml.load(inputStream);
    return tablesConfig.stream()
        .map(Map::entrySet)
        .map(Collection::stream)
        .map(Stream::findFirst)
        .map(Optional::orElseThrow)
        .map(config ->
            new Table(
                config.getKey(),
                parseExclude(config),
                parseRowProcessors(config),
                parseFields(config),
                repository.columns(config.getKey())
            )
        ).toList();
  }

  private Boolean parseExclude(Entry<String, Map<String, Object>> tableEntry) {
    return Optional.ofNullable(tableEntry.getValue().get("exclude"))
        .map(Boolean.class::cast)
        .orElse(false);
  }

  private List<RowPostProcessor> parseRowProcessors(Map.Entry<String, Map<String, Object>> tableEntry) {
    @SuppressWarnings("unchecked")
    var processors = (List<String>) tableEntry.getValue().get("processors");
    if (null == processors) {
      return Collections.emptyList();
    }
    return processors.stream()
        .map(processor -> {
          try {
            Class<?> aClass = Class.forName(processor);
            return (RowPostProcessor) aClass.getDeclaredConstructor().newInstance();
          } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
          }
        })
        .toList();
  }

  @SuppressWarnings("unchecked")
  private List<Field> parseFields(Map.Entry<String, Map<String, Object>> tableEntry) {
    var fields = (List<Map<String, Map<String, Object>>>) tableEntry.getValue().get("fields");
    if (null == fields) {
      return Collections.emptyList();
    }
    var columns = repository.columns(tableEntry.getKey());
    return fields.stream()
        .map(f -> f.entrySet().stream().findFirst().orElseThrow())
        .map(fieldEntry -> new Field(
                fieldEntry.getKey(),
                columns.stream()
                    .filter(column -> column.name().equals(fieldEntry.getKey()))
                    .findFirst()
                    .map(Column::position)
                    .orElseThrow(() -> new IllegalArgumentException(
                        "Column %s.%s not found!".formatted(tableEntry.getKey(), fieldEntry.getKey()))
                    ),
                parseFieldCrypt(fieldEntry.getValue()),
                parseFieldBase64(fieldEntry.getValue()),
                parseFieldGenerator(fieldEntry.getValue())
            )
        ).toList();
  }

  private boolean parseFieldCrypt(Map<String, Object> fieldConfig) {
    return Optional.ofNullable(fieldConfig.get("crypt"))
        .map(Boolean.class::cast)
        .orElse(false);
  }

  private boolean parseFieldBase64(Map<String, Object> fieldConfig) {
    return Optional.ofNullable(fieldConfig.get("base64"))
        .map(Boolean.class::cast)
        .orElse(false);
  }

  @SuppressWarnings("unchecked")
  private FieldGenerator parseFieldGenerator(Map<String, Object> fieldConfig) {
    var generator = (Map<String, Object>) fieldConfig.get("generator");
    if (null == generator) {
      return null;
    }
    try {
      Class<?> aClass = Class.forName((String) generator.get("name"));
      var params = (Map<String, Object>) generator.get("params");
      if (null != params) {
        return (FieldGenerator) aClass.getDeclaredConstructor(Map.class).newInstance(params);
      } else {
        return (FieldGenerator) aClass.getDeclaredConstructor().newInstance();
      }
    } catch (ReflectiveOperationException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
