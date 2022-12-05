// package xxx;
//
// import org.springframework.core.convert.converter.Converter;
//
// import java.util.Arrays;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;
//
// /**
//  * GET/POST 表单 枚举code -> 枚举
//  *
//  * @author woieha320r
//  */
// public class CodeToEnumConverter<T extends BaseEnum> implements Converter<Integer, T> {
//     private final Map<Integer, T> enumMapsWithCode;
//
//     public CodeToEnumConverter(Class<T> enumClass) {
//         enumMapsWithCode = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toMap(
//                 BaseEnum::getCode,
//                 enumObj -> enumObj,
//                 (v1, v2) -> v2
//         ));
//     }
//
//     @Override
//     public T convert(Integer source) {
//         return Optional.ofNullable(enumMapsWithCode.get(source))
//                 .orElseThrow(() -> new ParamException("枚举code不存在"));
//     }
// }