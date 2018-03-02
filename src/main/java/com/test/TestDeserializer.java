package com.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde2.Deserializer;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory.ObjectInspectorOptions;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class TestDeserializer implements Deserializer {
	private static List<String> FieldNames = new ArrayList<String>();
	private static List<ObjectInspector> FieldNamesObjectInspectors = new ArrayList<ObjectInspector>();
	static {
		FieldNames.add("time");
		FieldNamesObjectInspectors
				.add(ObjectInspectorFactory.getReflectionObjectInspector(Long.class, ObjectInspectorOptions.JAVA));
		FieldNames.add("userid");
		FieldNamesObjectInspectors
				.add(ObjectInspectorFactory.getReflectionObjectInspector(Integer.class, ObjectInspectorOptions.JAVA));
		FieldNames.add("host");
		FieldNamesObjectInspectors
				.add(ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorOptions.JAVA));
		FieldNames.add("path");
		FieldNamesObjectInspectors
				.add(ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorOptions.JAVA));
	}

	@Override
	public Object deserialize(Writable blob) {
		try {
			if (blob instanceof Text) {
				String line = ((Text) blob).toString();
				if (line == null)
					return null;
				String[] field = line.split("/t");
				if (field.length != 3) {
					return null;
				}
				List<Object> result = new ArrayList<Object>();
				URL url = new URL(field[2]);
				Long time = Long.valueOf(field[0]);
				Integer userid = Integer.valueOf(field[1]);
				result.add(time);
				result.add(userid);
				result.add(url.getHost());
				result.add(url.getPath());
				return result;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ObjectInspector getObjectInspector() throws SerDeException {
		return ObjectInspectorFactory.getStandardStructObjectInspector(FieldNames, FieldNamesObjectInspectors);
	}

	@Override
	public SerDeStats getSerDeStats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(Configuration arg0, Properties arg1) throws SerDeException {
		// TODO Auto-generated method stub

	}

}
