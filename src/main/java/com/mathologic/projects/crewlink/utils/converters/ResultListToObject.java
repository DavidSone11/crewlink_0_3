package com.mathologic.projects.crewlink.utils.converters;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.mathologic.projects.crewlink.models.Day;

public class ResultListToObject {
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertAll(List<Object[]> list, Class<?> c){
		List<T> listResult = new ArrayList<T>();
		for(Object[] o : list){
			try {
				listResult.add((T)instantiate(o,c));
			} catch (Exception e) {
				System.out.println("ERROR Occured in table conversion. "+e.getMessage());
			}
		}
		return listResult;
	}
	static Object convert(Class<?> target, Object in) {
		if(in==null)
			return null;
		String s = in.toString();
	    if (target == Object.class || target == String.class || s == null) {
	        return s;
	    }
	    if (target == Character.class || target == char.class) {
	        return s.charAt(0);
	    }
	    if (target == Byte.class || target == byte.class) {
	        return Byte.parseByte(s);
	    }
	    if (target == Short.class || target == short.class) {
	        return Short.parseShort(s);
	    }
	    if (target == Integer.class || target == int.class) {
	        return Integer.parseInt(s);
	    }
	    if (target == Long.class || target == long.class) {
	        return Long.parseLong(s);
	    }
	    if (target == Float.class || target == float.class) {
	        return Float.parseFloat(s);
	    }
	    if (target == Double.class || target == double.class) {
	        return Double.parseDouble(s);
	    }
	    if (target == Boolean.class || target == boolean.class) {
	        return Boolean.parseBoolean(s);
	    }
	    if (target == Day.class){
	    	return Day.values()[Integer.parseInt(s)];
	    }
	    throw new IllegalArgumentException("Don't know how to convert to " + target);
	}
	static Object instantiate(Object[] args, Class<?> c) throws Exception {
	    

	    // Search for an "appropriate" constructor.
	    for (Constructor<?> ctor : c.getConstructors()) {
	        Class<?>[] paramTypes = ctor.getParameterTypes();

	        // If the arity matches, let's use it.
	        if (args.length == paramTypes.length) {

	            // Convert the String arguments into the parameters' types.
	            Object[] convertedArgs = new Object[args.length];
	            for (int i = 0; i < convertedArgs.length; i++) {
	                convertedArgs[i] = convert(paramTypes[i], args[i]);
	            }

	            // Instantiate the object with the converted arguments.
	            return ctor.newInstance(convertedArgs);
	        }
	    }

	    throw new IllegalArgumentException("Don't know how to instantiate " + c.getCanonicalName());
	}

}
