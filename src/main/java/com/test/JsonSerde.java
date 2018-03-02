package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.AbstractDeserializer;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.StructTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.json.JSONObject;
import com.test.objectinspector.JsonObjectInspectorFactory;
import com.test.objectinspector.JsonStructOIOptions;

public class JsonSerde extends AbstractDeserializer {

	private static final Logger LOG = LoggerFactory.getLogger(JsonSerde.class);

	List<String> columnNames;
	List<TypeInfo> columnTypes;
	StructTypeInfo rowTypeInfo;
	private SerDeStats stats;
	JsonStructOIOptions options;
	StructObjectInspector rowObjectInspector;
	boolean[] columnSortOrderIsDesc;
	boolean ignoreMalformedJson = false;
	public static final String PROP_IGNORE_MALFORMED_JSON = "ignore.malformed.json";

	@Override
	public Object deserialize(Writable blob) throws SerDeException {
		Object jObj = null;
		try {
			if (blob instanceof Text) {
				String afString = ((Text) blob).toString();
				if (afString == null)
					return null;
				jObj = new JSONObject(afString);
				JSONObject jsonObject = (JSONObject) jObj;
				LOG.debug("jsonobject:" + jObj.toString());
			}
		} catch (Exception e) {
			LOG.debug("error to convert json");
		}

		return jObj;
	}

	@Override
	public ObjectInspector getObjectInspector() throws SerDeException {
		return rowObjectInspector;
	}

	@Override
	public SerDeStats getSerDeStats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize(Configuration conf, Properties tbl) throws SerDeException {
		LOG.debug("Initializing SerDe");
		// Get column names and sort order
		String columnNameProperty = tbl.getProperty(serdeConstants.LIST_COLUMNS);
		String columnTypeProperty = tbl.getProperty(serdeConstants.LIST_COLUMN_TYPES);

		LOG.debug("columns " + columnNameProperty + " types " + columnTypeProperty);

		// all table column names
		if (columnNameProperty.length() == 0) {
			columnNames = new ArrayList<String>();
		} else {
			columnNames = Arrays.asList(columnNameProperty.split(","));
		}

		// all column types
		if (columnTypeProperty.length() == 0) {
			columnTypes = new ArrayList<TypeInfo>();
		} else {
			columnTypes = TypeInfoUtils.getTypeInfosFromTypeString(columnTypeProperty);
		}
		assert (columnNames.size() == columnTypes.size());

		stats = new SerDeStats();

		// Create row related objects
		rowTypeInfo = (StructTypeInfo) TypeInfoFactory.getStructTypeInfo(columnNames, columnTypes);

		// build options
		options = new JsonStructOIOptions(getMappings(tbl));

		rowObjectInspector = (StructObjectInspector) JsonObjectInspectorFactory
				.getJsonObjectInspectorFromTypeInfo(rowTypeInfo, options);

		// Get the sort order
		String columnSortOrder = tbl.getProperty(serdeConstants.SERIALIZATION_SORT_ORDER);
		columnSortOrderIsDesc = new boolean[columnNames.size()];
		for (int i = 0; i < columnSortOrderIsDesc.length; i++) {
			columnSortOrderIsDesc[i] = (columnSortOrder != null && columnSortOrder.charAt(i) == '-');
		}

		// other configuration
		ignoreMalformedJson = Boolean.parseBoolean(tbl.getProperty(PROP_IGNORE_MALFORMED_JSON, "false"));

	}

	public static final String PFX = "mapping.";

	private Map<String, String> getMappings(Properties tbl) {
		int n = PFX.length();
		Map<String, String> mps = new HashMap<String, String>();

		for (Object o : tbl.keySet()) {
			if (!(o instanceof String)) {
				continue;
			}
			String s = (String) o;

			if (s.startsWith(PFX)) {
				mps.put(s.substring(n), tbl.getProperty(s).toLowerCase());
			}
		}
		return mps;
	}

}
