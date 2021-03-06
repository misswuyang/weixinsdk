/* Copyright(c)2012 www.aiisen.com
 * Email:admin@aiisen.com
 * 
 */

package com.aiisen.weixin.httpclient;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * <p>
 * </p>
 * 
 * @author Wu,Yang
 * @date 2014年4月18日 下午9:07:23
 */
public class XmlResponseHandler implements ResponseHandler<Document> {

	@Override
	public Document handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();

		if (status < 200 && status >= 300) {
			throw new ClientProtocolException("Unexpected response status: " + status);
		} else {

		}

		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}

		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			ContentType contentType = ContentType.getOrDefault(entity);
			if (!contentType.equals(ContentType.APPLICATION_XML)) {
				throw new ClientProtocolException("Unexpected content type:" + contentType);
			}
			Charset charset = contentType.getCharset();
			if (charset == null) {
				charset = Consts.UTF_8;
			}
			return docBuilder.parse(entity.getContent(), charset.name());
		} catch (ParserConfigurationException ex) {
			throw new IllegalStateException(ex);
		} catch (SAXException ex) {
			throw new ClientProtocolException("Malformed XML document", ex);
		}
	}

}
