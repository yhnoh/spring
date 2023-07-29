package com.example.springbatchitemreader.paging_item_reader;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CustomPagingItemReaderTest {


    @Test
    void pagingTest() throws Exception {
        CustomPagingItemReader customPagingItemReader = new CustomPagingItemReader(2);
        customPagingItemReader.setSaveState(false);
        customPagingItemReader.open(null);


        Member read1 = customPagingItemReader.read();
        Member read2 = customPagingItemReader.read();
        Member read3 = customPagingItemReader.read();
        Member read4 = customPagingItemReader.read();
        Member read5 = customPagingItemReader.read();
        Member read6 = customPagingItemReader.read();

        customPagingItemReader.close();

        assertThat(read1).isNotNull();
        assertThat(read2).isNotNull();
        assertThat(read3).isNotNull();
        assertThat(read4).isNotNull();
        assertThat(read5).isNotNull();
        assertThat(read6).isNull();


    }

}