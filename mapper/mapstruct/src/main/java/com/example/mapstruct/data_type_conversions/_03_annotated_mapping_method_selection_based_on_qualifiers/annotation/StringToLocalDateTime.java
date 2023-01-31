package com.example.mapstruct.data_type_conversions._03_annotated_mapping_method_selection_based_on_qualifiers.annotation;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface StringToLocalDateTime {

}
