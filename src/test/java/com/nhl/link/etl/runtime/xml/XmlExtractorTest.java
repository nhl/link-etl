package com.nhl.link.etl.runtime.xml;

import com.nhl.link.etl.RowAttribute;
import com.nhl.link.etl.RowReader;
import com.nhl.link.etl.extract.ExtractorParameters;
import com.nhl.link.etl.runtime.http.HttpConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XmlExtractorTest {

	private XmlExtractor xmlExtractor;

	private HttpConnector httpConnectorMock;

	private InputStream inputStreamMock;

	private XPathExpression xPathExpressionMock;

	@Before
	public void setUpXmlExtractor() throws IOException {
		inputStreamMock = mock(InputStream.class);
		httpConnectorMock = mock(HttpConnector.class);
		when(httpConnectorMock.getInputStream()).thenReturn(inputStreamMock);
		xPathExpressionMock = mock(XPathExpression.class);
		xmlExtractor = new XmlExtractor(httpConnectorMock, new RowAttribute[0], xPathExpressionMock);
	}

	@Test
	public void testGetReader() throws Exception {
		RowReader reader = xmlExtractor.getReader(new ExtractorParameters());
		verify(xPathExpressionMock).evaluate(argThat(new ArgumentMatcher<InputSource>() {
			@Override
			public boolean matches(Object argument) {
				return ((InputSource) argument).getByteStream() == inputStreamMock;
			}
		}), eq(XPathConstants.NODESET));
		assertNotNull(reader);
	}
}