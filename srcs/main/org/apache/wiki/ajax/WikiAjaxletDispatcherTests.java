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

import static org.junit.jupiter.api.Assertions.*;
import static rebound.util.collections.BasicCollectionUtilities.*;
import org.junit.Test;
import rebound.util.collections.PairOrdered;

/**
 * @since 2.10.2-svn10
 */
public class WikiAjaxletDispatcherTests
{
	@Test
	public void testParsingName() throws Exception
	{
		final PairOrdered<String, String>[] testdata = new PairOrdered[]
		{
		pair("", null),
		pair("/", null),
		pair("/aj", null),
		pair("/ajax", null),
		pair("/ajax/", null),
		
		pair("/ajax/MyPlugin", "MyPlugin"),
		pair("/ajax/MyPlugin/", "MyPlugin"),
		
		pair("/ajax/MyPlugin/Friend", "MyPlugin"),
		pair("/ajax/MyPlugin/Friend/", "MyPlugin"),
		
		
		pair("/ajax/λ/Friend", "λ"),
		pair("/ajax/%CE%BB/Friend", "λ"),
		pair("/ajax/%ce%bb/Friend", "λ"),
		pair("/ajax/%Ce%bB/Friend", "λ"),
		
		
		pair("+", null),
		pair("+/", null),
		pair("/+", null),
		pair("/aj+", null),
		pair("/aj+ax", null),
		pair("/ajax+/", null),
		pair("/ajax+/+", null),
		pair("/ajax/+", " "),
		
		pair("/ajax/My+Plugin", "My Plugin"),
		pair("/ajax/My+Plugin/", "My Plugin"),
		
		pair("/ajax/MyPlugin/Friend+", "MyPlugin"),
		pair("/ajax/MyPlugin/Friend+/", "MyPlugin"),
		
		pair("/ajax/My+Plugin/Friend", "My Plugin"),
		pair("/ajax/My+Plugin/Friend/", "My Plugin"),
		
		pair("/ajax/My+Plugin/Friend+", "My Plugin"),
		pair("/ajax/My+Plugin/Friend+/", "My Plugin"),
		
		
		pair("%2F", null),
		pair("%2F/", null),
		pair("/%2F", null),
		pair("/aj%2F", null),
		pair("/aj%2Fax", null),
		pair("/ajax%2F/", null),
		pair("/ajax%2F/%2F", null),
		pair("/ajax/%2F", "/"),
		
		pair("/ajax/My%2FPlugin", "My/Plugin"),
		pair("/ajax/My%2FPlugin/", "My/Plugin"),
		pair("/ajax/My%2FPlugin/A", "My/Plugin"),
		pair("/ajax/My%2FPlugin/%2F", "My/Plugin"),
		pair("/ajax/MyPlugin%2F", "MyPlugin/"),
		pair("/ajax/MyPlugin%2F/", "MyPlugin/"),
		pair("/ajax/MyPlugin%2F/A", "MyPlugin/"),
		pair("/ajax/MyPlugin%2F/%2F", "MyPlugin/"),
		pair("/ajax/My%2FPlugin%2F", "My/Plugin/"),
		pair("/ajax/My%2FPlugin%2F/", "My/Plugin/"),
		pair("/ajax/My%2FPlugin%2F/A", "My/Plugin/"),
		pair("/ajax/My%2FPlugin%2F/%2F", "My/Plugin/"),
		
		pair("/ajax/MyPlugin/Friend%2F", "MyPlugin"),
		pair("/ajax/MyPlugin/Friend%2F/", "MyPlugin"),
		
		pair("/ajax/My%2FPlugin/Friend", "My/Plugin"),
		pair("/ajax/My%2FPlugin/Friend/", "My/Plugin"),
		
		pair("/ajax/My%2FPlugin/Friend%2F", "My/Plugin"),
		pair("/ajax/My%2FPlugin/Friend%2F/", "My/Plugin"),
		};
		
		
		for (PairOrdered<String, String> pair : testdata)
		{
			final String ajaxletAction = WikiAjaxletDispatcher.getAjaxletName(pair.getA(), "/ajax/");
			assertEquals(pair.getB(), ajaxletAction);
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
		pair("/ajax/MyPlugin/", null),
		
		pair("/ajax/MyPlugin/Friend", "Friend"),
		pair("/ajax/MyPlugin/Friend/", "Friend"),
		
		
		pair("/ajax/MyPlugin/λ", "λ"),
		pair("/ajax/MyPlugin/%CE%BB","λ"),
		pair("/ajax/MyPlugin/%ce%bb", "λ"),
		pair("/ajax/MyPlugin/%Ce%bB", "λ"),
		
		
		pair("+", null),
		pair("+/", null),
		pair("/+", null),
		pair("/aj+", null),
		pair("/aj+ax", null),
		pair("/ajax+/", null),
		pair("/ajax+/+", null),
		
		pair("/ajax/My+Plugin", null),
		pair("/ajax/My+Plugin/", null),
		
		pair("/ajax/MyPlugin/Friend+", "Friend "),
		pair("/ajax/MyPlugin/Friend+/", "Friend "),
		
		pair("/ajax/My+Plugin/Friend", "Friend"),
		pair("/ajax/My+Plugin/Friend/", "Friend"),
		
		pair("/ajax/My+Plugin/Friend+", "Friend "),
		pair("/ajax/My+Plugin/Friend+/", "Friend "),
		
		
		pair("%2F", null),
		pair("%2F/", null),
		pair("/%2F", null),
		pair("/aj%2F", null),
		pair("/aj%2Fax", null),
		pair("/ajax%2F/", null),
		pair("/ajax%2F/%2F", null),
		
		pair("/ajax/My%2FPlugin", null),
		pair("/ajax/My%2FPlugin/", null),
		
		pair("/ajax/MyPlugin/Friend%2F", "Friend/"),
		pair("/ajax/MyPlugin/Friend%2F/", "Friend/"),
		
		pair("/ajax/My%2FPlugin/Friend", "Friend"),
		pair("/ajax/My%2FPlugin/Friend/", "Friend"),
		
		pair("/ajax/My%2FPlugin/Friend%2F", "Friend/"),
		pair("/ajax/My%2FPlugin/Friend%2F/", "Friend/"),
		};
		
		
		for (PairOrdered<String, String> pair : testdata)
		{
			final String ajaxletAction = WikiAjaxletDispatcher.getAjaxletAction(pair.getA(), "/ajax/");
			assertEquals(pair.getB(), ajaxletAction);
		}
	}
	
	
	
	@Test
	public void testRegistering() throws Exception
	{
		String n = "λfoo";
		WikiAjaxlet a = (request, response, actionName, params) -> {};
		
		assertNull(WikiAjaxletDispatcher.findAjaxletByName("dslfkjsflksdjfdslkjfdslfdskjlfdskjf"));
		assertNull(WikiAjaxletDispatcher.findAjaxletByName(n));
		
		WikiAjaxletDispatcher.registerAjaxlet(n, a);
		
		assertNull(WikiAjaxletDispatcher.findAjaxletByName("dslfkjsflksdjfdslkjfdslfdskjlfdskjf"));
		assertTrue(WikiAjaxletDispatcher.findAjaxletByName(n) == a);
	}
}
