/*
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
 */
package org.apache.wiki.ajax;

import static rebound.util.collections.BasicCollectionUtilities.*;
import org.apache.wiki.plugin.plugins.SampleAjaxPlugin;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import rebound.util.collections.PairOrdered;

/**
 * @since 2.10.2-svn10
 */
public class WikiAjaxletDispatcherTests
{
	@Test
	public void testParsingName() throws Exception
	{
		final String[] uris = new String[]
		{
		"/ajax/MyPlugin",
		"/ajax/MyPlugin/",
		"/ajax/MyPlugin/Friend",
		"/ajax/MyPlugin?",
		"/ajax/MyPlugin?param=123&param=231",
		"/ajax/MyPlugin#hashCode?param=123&param=231",
		"/ajax/MyPlugin?param=123&param=231#hashCode",
		
		//TODO-PP Why does it need to handle these? Those are URLs not HTTP URI's! XD
		//	"http://google.com.au/test/ajax/MyPlugin#hashCode?param=123&param=231",
		//	"/test//ajax/MyPlugin#hashCode?param=123&param=231",
		//	"http://localhost:8080/ajax/MyPlugin#hashCode?param=123&param=231"
		};
		
		for (final String uri : uris)
		{
			final String ajaxletName = WikiAjaxletDispatcher.getAjaxletName(uri, "/ajax/");
			Assertions.assertEquals("MyPlugin", ajaxletName);
		}
	}
	
	
	
	@Test
	public void testParsingAction() throws Exception
	{
		final PairOrdered<String, String>[] testdata = new PairOrdered[]
		{
		pair("", null),
		pair("/", null),
		pair("/aj", null),
		pair("/ajax", null),
		pair("/ajax/", null),
		
		pair("/ajax/MyPlugin", null),
		pair("/ajax/MyPlugin?", null),
		pair("/ajax/MyPlugin?param=123&param=231", null),
		pair("/ajax/MyPlugin#hashCode?param=123&param=231", null),
		pair("/ajax/MyPlugin?param=123&param=231#hashCode", null),
		pair("/ajax/MyPlugin/", null),
		pair("/ajax/MyPlugin/?", null),
		pair("/ajax/MyPlugin/?param=123&param=231", null),
		pair("/ajax/MyPlugin/#hashCode?param=123&param=231", null),
		pair("/ajax/MyPlugin/?param=123&param=231#hashCode", null),
		
		pair("/ajax/MyPlugin/Friend", "Friend"),
		pair("/ajax/MyPlugin/Friend?", "Friend"),
		pair("/ajax/MyPlugin/Friend?param=123&param=231", "Friend"),
		pair("/ajax/MyPlugin/Friend#hashCode?param=123&param=231", "Friend"),
		pair("/ajax/MyPlugin/Friend?param=123&param=231#hashCode", "Friend"),
		pair("/ajax/MyPlugin/Friend/", "Friend"),
		pair("/ajax/MyPlugin/Friend/?", "Friend"),
		pair("/ajax/MyPlugin/Friend/?param=123&param=231", "Friend"),
		pair("/ajax/MyPlugin/Friend/#hashCode?param=123&param=231", "Friend"),
		pair("/ajax/MyPlugin/Friend/?param=123&param=231#hashCode", "Friend"),
		};
		
		for (PairOrdered<String, String> pair : testdata)
		{
			final String ajaxletAction = WikiAjaxletDispatcher.getAjaxletAction(pair.getA(), "/ajax/");
			Assertions.assertEquals(pair.getB(), ajaxletAction);
		}
	}
	
	
	
	@Test
	public void testRegistering() throws Exception
	{
		// The plugin SampleAjaxPlugin
		String n = SampleAjaxPlugin.AJAXLET_NAME;
		WikiAjaxletDispatcher.registerAjaxlet(n, new SampleAjaxPlugin());
		final WikiAjaxlet ajaxlet = WikiAjaxletDispatcher.findAjaxletByName(n);
		Assertions.assertNotNull(ajaxlet);
		Assertions.assertTrue(ajaxlet instanceof SampleAjaxPlugin);
		
		Assertions.assertNull(WikiAjaxletDispatcher.findAjaxletByName("dslfkjsflksdjfdslkjfdslfdskjlfdskjf"));
	}
}
