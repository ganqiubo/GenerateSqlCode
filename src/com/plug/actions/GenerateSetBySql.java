package com.plug.actions;
import java.sql.ResultSet;
import java.util.HashMap;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

public class GenerateSetBySql implements IActionDelegate{

	public ICompilationUnit compUnit;
	public static HashMap<String, String> typeSignatures = new HashMap<String, String>(){{
		put("I", "Int");
		put("QString;", "String");
		put("QResultSet;", "String");
	}};

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		if(compUnit == null) {
			return;
		}
		try {
			IType[] types = compUnit.getAllTypes();
			IField[] iFields = null;
			IMethod[] iMethod = null;
			if(types != null && types.length > 0) {
				IMethod iMethod1 = types[0].getMethod("setBySql", new String[] {"QResultSet;"});
				if(iMethod1 != null && iMethod1.exists()) {
					deleteRawMethod(iMethod1, compUnit.getWorkingCopy(null));
					types = compUnit.getAllTypes();
				}
				iFields = types[0].getFields();
				iMethod = types[0].getMethods();
				/*for(int i = 0; i<iFields.length; i++ ) {
					System.out.println(iFields[i].getElementName() + " : "
							+ iFields[i].getTypeSignature());
				}*/
			}
			ICompilationUnit copyCompUnit = compUnit.getWorkingCopy(null); //获取副本 
			generateMethod(iFields, iMethod, copyCompUnit);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			compUnit.close();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		compUnit = null;
	}


	private void deleteRawMethod(IMethod iMethod, ICompilationUnit copy) {
		// TODO Auto-generated method stub
		IBuffer buffer;
		try {
			buffer = copy.getBuffer();
			buffer.replace(iMethod.getSourceRange().getOffset(), 
					iMethod.getSourceRange().getLength(), "");
			copy.reconcile(0, false, copy.getOwner(), null);
			copy.commitWorkingCopy(true, null);
	        copy.discardWorkingCopy();//删除副本  
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	public void generateMethod(IField[] iFields, IMethod[] iMethod, ICompilationUnit copy) {
		if(iFields == null || iFields.length <= 0) {
			return;
		}
		int index = 0;
		IBuffer buffer;
		try {
			if(iMethod == null || iMethod.length <= 0) {
				index = iFields[(iFields.length -1)].getSourceRange().getOffset()
						+ iFields[(iFields.length -1)].getSourceRange().getLength();
			}else {
				index = iMethod[(iMethod.length -1)].getSourceRange().getOffset()
						+ iMethod[(iMethod.length -1)].getSourceRange().getLength();
			}
			
			StringBuffer sb = new StringBuffer("\r\n\r\n\t@Override\r\n\tpublic void setBySql(ResultSet rs) {"
					+ "\r\n\t\t// TODO Auto-generated method stub\r\n\t\tsuper.setBySql(rs);"
					+ "\r\n\t\tif(rs == null) {\r\n\t\t\treturn;\r\n\t\t}");
			for (int i = 0; i < iFields.length; i++) {
				IField iField = iFields[i];
				String fieldType = typeSignatures.get(iField.getTypeSignature());
				String fieldStr = iField.getElementName();
				String mapFieldStr = getMapField(fieldStr);
				if(fieldType != null) {
					sb.append("\r\n\t\t" + fieldStr + " = get" + fieldType + "(rs, \"" + 
							mapFieldStr + "\");");
				}else {
					System.out.println("generateMethod: no matched TypeSignature found");
				}
			}
			sb.append("\r\n\t}");
			
			buffer = copy.getBuffer();
			buffer.replace(index, 0, sb.toString());//第二个参数给0就是插入,大于0的话就是替换
	        copy.reconcile(0, false, copy.getOwner(), null);
	        copy.createImport("java.sql.ResultSet", null, null);//还要把依赖的包导入，不然改了也报错  
	        copy.reconcile(0, false, copy.getOwner(), null);  
	        copy.commitWorkingCopy(true, null);
	        copy.discardWorkingCopy();//删除副本  
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String getMapField(String fieldStr) {
		// TODO Auto-generated method stub
		String mapFieldStr = fieldStr;
		for (int i = 0; i < mapFieldStr.length(); i++) {
			char c = mapFieldStr.charAt(i);
			if(Character.isUpperCase(c)) {
				String upper = ("" + c);
				String lower = "_" + upper.toLowerCase();
				mapFieldStr = mapFieldStr.replace(upper, lower);
				i = 0;
			}
		}
		return mapFieldStr;
	}


	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		IStructuredSelection select = (IStructuredSelection) arg1;
		compUnit =(ICompilationUnit)select.getFirstElement();
	}
	
}
