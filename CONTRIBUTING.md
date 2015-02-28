Contributing
============
Thanks for considering to contribute your abilities and time to this project! Before you start we have a few important
things for you to know:

**Note:** You may only submit material that you own the required rights to. You are granting us an unlimited permission
to include your material in the product under the terms of the Apache License, Version 2.0.

Licensing
---------
All contributions to this repository are subject to the terms of the Apache License, Version 2.0 unless otherwise noted
in the submitted files (These notes have to be clearly visible at the top).

Every new file submitted to the repository must include the following copyright header:
```java
/*
 * Copyright 2015 [Your Name] <youremail@example.org>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```
If you are working on behalf of a company, please also add them to the copyright header.

Code Style
----------
Oracle coding standards apply mostly however we chose to apply the following additional terms:

**Indentations:** Tabs - Width: 8
**Braces:** On the same line
**Parenthesis:** Keep a space in front of them

Please also prefer the following styles:
```java
// do
void method () {
	if (condition) return;
}
// don't
void method () {
	if (!condition) {
		...
	}
}

// do
if (condition) method ();
// don't
if (condition) {
	method ();
}

Testing
-------
Before submitting make sure that your changes won't break anything! Even though somebody of our team will review your
contents before merging them, you're saving us a lot of time by testing your code.

Where possible write test cases!