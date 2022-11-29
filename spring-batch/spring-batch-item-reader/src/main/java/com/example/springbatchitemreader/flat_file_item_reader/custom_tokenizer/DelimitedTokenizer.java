package com.example.springbatchitemreader.flat_file_item_reader.custom_tokenizer;

import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.item.file.transform.LineTokenizer;

import java.util.ArrayList;

public class DelimitedTokenizer implements LineTokenizer {

    private String delimiter = ",";
    private String[] names = new String[]{"firstName",
            "middleInitial",
    "lastName","address", "city", "state", "zipCode"};

    private FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();

    @Override
    public FieldSet tokenize(String line) {
        String[] fields = line.split(delimiter);

        ArrayList<String> parsedFields = new ArrayList<>();

        for (int i = 0; i < fields.length; i++){
            if(i == 4){
                parsedFields.set(i -1, parsedFields.get(i - 1) + " " + fields[i]);
            }else{
                parsedFields.add(fields[i]);
            }
        }

        return fieldSetFactory.create(parsedFields.toArray(new String[0]), names);
    }
}
