package com.piggsoft.filter;

import com.piggsoft.filter.WXFilter;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;

/**
 * Created by user on 2015/11/23.
 */
@RunWith(EasyMockRunner.class)
public class WXFilterTest {

    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);


    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private FilterConfig mockConfig;
    @Mock
    private FilterChain mockFilterChain;
    @Mock
    private ServletContext servletContext;

    @TestSubject
    private WXFilter wxFilter = new WXFilter();


    @After
    public void tearDown(){
        verify(mockRequest);		//验证
        verify(mockResponse);
    }

    private void refreshContext() throws IOException {
        final InputStream in = ResourceUtils.getURL("classpath:test.xml").openStream();

        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
                .andReturn(null)
                .times(1, 2);

        expect(servletContext.getAttributeNames())
                .andReturn(new Enumeration<String>() {
                    @Override
                    public boolean hasMoreElements() {
                        return false;
                    }

                    @Override
                    public String nextElement() {
                        return null;
                    }
                })
                .times(1, 2);

        expect(mockConfig.getServletContext())
                .andReturn(servletContext)
                .times(1, 2);

        expect(mockConfig.getInitParameter("wxConfigLocations"))
                .andReturn(null)
                .times(1, 2);

        expect(mockRequest.getInputStream())
                .andReturn(new ServletInputStream() {
                    @Override
                    public int read() throws IOException {
                        return in.read();
                    }
                })
                .times(1, 2);

        expect(mockResponse.getOutputStream())
                .andReturn(new ServletOutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        System.out.write(b);
                    }
                })
                .times(1, 2);

        replay(servletContext);
        replay(mockConfig);
        replay(mockRequest);                    //回放
        replay(mockResponse);
    }

    @Test
    public void test01() throws IOException, ServletException {

        refreshContext();

        WXFilter wxFilter = new WXFilter();
        wxFilter.init(mockConfig);
        wxFilter.doFilter(mockRequest, mockResponse, mockFilterChain);

    }

}
