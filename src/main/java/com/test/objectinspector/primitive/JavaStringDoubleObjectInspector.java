/*======================================================================*
 * Copyright (c) 2011, OpenX Technologies, Inc. All rights reserved.    *
 *                                                                      *
 * Licensed under the New BSD License (the "License"); you may not use  *
 * this file except in compliance with the License. Unless required     *
 * by applicable law or agreed to in writing, software distributed      *
 * under the License is distributed on an "AS IS" BASIS, WITHOUT        *
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.     *
 * See the License for the specific language governing permissions and  *
 * limitations under the License. See accompanying LICENSE file.        *
 *======================================================================*/

package com.test.objectinspector.primitive;

import org.apache.hadoop.hive.serde2.objectinspector.primitive.AbstractPrimitiveJavaObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.SettableDoubleObjectInspector;
import org.apache.hadoop.io.DoubleWritable;

/**
 *
 * @author rcongiu
 */
public class JavaStringDoubleObjectInspector extends AbstractPrimitiveJavaObjectInspector
        implements SettableDoubleObjectInspector {

    public JavaStringDoubleObjectInspector() {
        super(TypeEntryShim.doubleType);
    }

    public Object getPrimitiveWritableObject(Object o) {
        if(o == null) return null;
        
        if(o instanceof String) {
           return new DoubleWritable(Double.parseDouble((String)o)); 
        } else {
          return new DoubleWritable(((Double) o));
        }
    }

    public double get(Object o) {
        
        if(o instanceof String) {
           return Double.parseDouble((String)o); 
        } else {
          return (((Double) o));
        }
    }

    public Object create(double value) {
        return value;
    }

    public Object set(Object o, double value) {
        return value;
    }
    
}
