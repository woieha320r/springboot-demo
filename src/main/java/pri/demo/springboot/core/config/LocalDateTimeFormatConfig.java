package pri.demo.springboot.core.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 按照spring.jackson.date-format格式化LocalDateTime
 *
 * @author woieha320r
 */
@Configuration
@ConditionalOnProperty("spring.jackson.date-format")
public class LocalDateTimeFormatConfig {

    @Bean
    DateTimeFormatter dateTimeFormatter(@Value("${spring.jackson.date-format}") String dateFormat) {
        return DateTimeFormatter.ofPattern(dateFormat);
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer localDateTimeMapperBuilder(DateTimeFormatter dateTimeFormatter) {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
            jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        };
    }
}


//官方文档写法，但无法与spring.jackson.date-format同步
// @JsonComponent
// public class LocalDateTimeFormatConfig {
//
//     private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//     public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
//         @Override
//         public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
//             jgen.writeString(value.format(dateTimeFormatter));
//         }
//     }
//
//     public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
//         @Override
//         public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//             return LocalDateTime.parse(p.getText(), dateTimeFormatter);
//         }
//     }
//
// }