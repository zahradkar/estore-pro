package eu.martin.store.product;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
class MeasureUnitConverter implements AttributeConverter<MeasureUnit, String> {
    @Override
    public String convertToDatabaseColumn(MeasureUnit unit) {
        if (unit == null)
            return null;

        return unit.getCode();
    }

    @Override
    public MeasureUnit convertToEntityAttribute(String code) {
        if (code == null)
            return null;

        return Stream.of(MeasureUnit.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

