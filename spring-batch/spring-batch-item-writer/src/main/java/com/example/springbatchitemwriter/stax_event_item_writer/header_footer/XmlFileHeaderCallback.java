package com.example.springbatchitemwriter.stax_event_item_writer.header_footer;

import org.springframework.batch.item.xml.StaxWriterCallback;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * StaxWriterCallback 인터페이스는 현재 xml 문서에 xml을 추가하는데 사용되는 write 메서드를 가지고 있다.
 * StaxEventItemWriter의 headerCallback, footerCallback 메서드를 통해서 headr, footer를 구성할 수 있다.
 */
@Component
public class XmlFileHeaderCallback implements StaxWriterCallback {

    @Override
    public void write(XMLEventWriter writer) throws IOException {
        XMLEventFactory factory = XMLEventFactory.newInstance();

        try {
            writer.add(factory.createStartElement("", "", "identification"));
            writer.add(factory.createStartElement("", "", "author"));
            writer.add(factory.createAttribute("name", "Michael Minella"));
            writer.add(factory.createEndElement("", "", "author"));
            writer.add(factory.createEndElement("", "", "identification"));
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }
}
