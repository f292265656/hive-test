package com.test;

import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde2.AbstractDeserializer;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.Writable;

public class Test extends AbstractDeserializer{

    @Override
    public void initialize(Configuration conf, Properties tbl) throws SerDeException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object deserialize(Writable blob) throws SerDeException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectInspector getObjectInspector() throws SerDeException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SerDeStats getSerDeStats() {
        // TODO Auto-generated method stub
        return null;
    }

}
