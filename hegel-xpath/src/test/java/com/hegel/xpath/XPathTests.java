package com.hegel.xpath;import org.junit.jupiter.api.Disabled;import org.junit.jupiter.api.Test;import java.net.MalformedURLException;import java.net.URL;import static java.nio.file.Paths.get;import static java.util.stream.Collectors.joining;import static org.hamcrest.MatcherAssert.assertThat;import static org.hamcrest.core.Is.is;class XPathTests {    private static final String XML_FILE_PATH = "src/test/resources/document.xml";    private static final String XML = "<beans xmlns=\"http://www.springframework.org/schema/beans\"><!-- TpEvent construction --><bean id=\"tpEventMessageConverter\" class=\"com.db.swdealer.bbgconnector.jms.impl.TpEventMessageConverterImpl\" init-method=\"init\"><property name=\"packageName\" value=\"com.db.rtp.tpmon.tpevent\"/></bean><bean id=\"tpEventObjectFactory\" class=\"com.db.rtp.tpmon.tpevent.ObjectFactory\"/><bean id=\"upstreamNewMessageEvent\" class=\"com.db.swdealer.bbgconnector.tpevent.TpEventBuilder\" init-method=\"init\"><property name=\"fixml\" value=\"true\"/><property name=\"catalogCode\" value=\"6151\"/><property name=\"codeText\" value=\"Received new upstream message. mpp_id:%XML, version:%XML, product:%XML \"/><property name=\"eventFactory\" ref=\"tpEventObjectFactory\"/></bean><!-- Filters --><bean id=\"nullFilter\" class=\"com.db.swdealer.bbgconnector.nodeStream.impl.NullObjectFilter\"/><!-- Camel context section --><camel:camelContext xmlns:camel=\"http://camel.apache.org/schema/spring\"><camel:route><camel:from uri=\"bean:withoutCloneProducer?method=produceMessage\"/><camel:nodeStream><camel:method bean=\"nullFilter\" method=\"isAllowed\"/><camel:to uri=\"bean:notificationCache?method=saveNotification\"/><camel:to uri=\"bean:notificationStatusService2?method=markSentAndSave\"/><camel:to uri=\"bean:waitObject?method=notify\"/></camel:nodeStream></camel:route></camel:camelContext><!-- Test beans section --><bean id=\"waitObject\" class=\"com.db.swdealer.bbgconnector.sync.SyncObject\"/></beans>";    private static final String X_PATH = "//*[namespace-uri()='http://www.springframework.org/schema/beans']/@id";    @Test    void xPathQueryAsObject() {        assertThat(                new XPathQueryExecutor().stream(XML, X_PATH).collect(joining(", ")),                is("tpEventMessageConverter, tpEventObjectFactory, upstreamNewMessageEvent, nullFilter, waitObject"));    }    @Test    void invokeRootNamespace() {        assertThat(                new Xml(get(XML_FILE_PATH)).getRootNamespaceURI().orElseThrow(RuntimeException::new),                is("http://www.springframework.org/schema/beans"));    }    @Test    void xPathQuery() {        assertThat(                new XPathQuery(X_PATH).stream(get(XML_FILE_PATH)).collect(joining(", ")),                is("tpEventMessageConverter, tpEventObjectFactory, upstreamNewMessageEvent, nullFilter, waitObject"));    }    @Disabled    @Test    void xPathQueryToURL() throws MalformedURLException {        //noinspection ConstantConditions        assertThat(                Xhtml.from(                        new URL("http://vlapin.ru/"))                        .getRootNamespaceURI()                        .get(),                is("http://www.w3.org/1999/xhtml"));    }}