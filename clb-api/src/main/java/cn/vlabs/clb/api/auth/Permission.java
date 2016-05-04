/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */

package cn.vlabs.clb.api.auth;

import java.security.Principal;
import java.util.HashSet;

public class Permission {
	public Permission(Principal prin, String[] ops){
		this.prin=prin;
		setOperations(ops);
	}
	public Permission(){
	}
	public void setPrincipal(Principal prin){
		this.prin=prin;
	}
	public void setOperations(String[] ops){
		this.opset=new HashSet<String>();
		for (String op:ops){
			this.opset.add(op);
		}
	}
	public Principal getPrincipal(){
		return prin;
	}
	public String[] getOperations(){
		return opset.toArray(new String[opset.size()]);
	}
	public boolean equals(Object o){
		if (o==null)
			return false;
		if (o==this)
			return true;
		if (o instanceof Permission){
			Permission p = (Permission)o;
			return prin.equals(p.prin) && opset.equals(p.opset);
		}
		return false;
	}
	public int hashCode(){
		return prin.hashCode()+opset.hashCode();
	}
	public static final String READ="view";
	public static final String WRITE="update";
	public static final String GRANT="grant";
	public static final String DELETE="delete";
	private HashSet<String> opset;
	private Principal prin;
}
