/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.buzz.model;

import com.google.api.client.util.Key;

/**
 * Buzz Object.
 * 
 * <p>
 * The JSON of a typical Buzz object looks like this:
 * 
 * <pre>
 * <code>{
 *  content: "Hey, this is my first Buzz Post!",
 *  ...
 * }</code>
 * </pre>
 * 
 * @author Yaniv Inbar
 */
public class BuzzObject {

  /** HTML content. */
  @Key
  public String content;
}
