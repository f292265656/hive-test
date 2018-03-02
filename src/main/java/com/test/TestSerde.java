package com.test;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSerde implements Deserializer {
	private List<String> structFieldNames = new ArrayList<String>();
	private List<ObjectInspector> structFieldObjectInspectors = new ArrayList<ObjectInspector>();

	private static final Logger log = LoggerFactory.getLogger(TestSerde.class);

	@Override
	public Object deserialize(Writable blob) throws SerDeException {
		List<Object> result = new ArrayList<Object>();
		if (blob instanceof Text) {
			String line = ((Text) blob).toString();
			log.debug("line : ++++++++ " + line);
			if (line == null)
				return null;
			// String[] field = StringUtils.split(line, "|");
			String[] field = line.split("\\|");
			log.debug("********** " + field.length);
			if (field.length != 3) {
				log.debug("**************** leng != 3");
				return null;
			}
			String afJson = field[2];
			log.debug("afJsonString : ++++++++ " + afJson);
			// AppsFlyerLogVo vo = JSON.parseObject(afJson,
			// AppsFlyerLogVo.class);
			// log.debug("AppsFlyerLogVo : ++++++++ "+vo.toString());
			// result.add(vo.getDeviceModel());
			// result.add(vo.getOperator());
		}

		return result;
	}

	@Override
	public ObjectInspector getObjectInspector() throws SerDeException {
		return ObjectInspectorFactory.getStandardStructObjectInspector(structFieldNames, structFieldObjectInspectors);
	}

	@Override
	public SerDeStats getSerDeStats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(Configuration arg0, Properties arg1) throws SerDeException {
		structFieldNames.add("device_model");
		structFieldObjectInspectors
				.add(ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorOptions.JAVA));
		structFieldNames.add("operator");
		structFieldObjectInspectors
				.add(ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorOptions.JAVA));

	}

}
