package io.github.repir.tools.collection;

import io.github.repir.tools.lib.Log;
import io.github.repir.tools.type.Tuple3;

/**
 * A TreeSet containing non-unique integers that are sorted descending
 * <p/>
 * @author jeroen
 */
public class ArrayMap4<K, V1, V2, V3> extends ArrayMap<K, Tuple3<V1, V2, V3>> {

   public static Log log = new Log(ArrayMap4.class);
   
   public ArrayMap4( ) {
      super();
   }
   
   public void add(K k, V1 v1, V2 v2, V3 v3) {
       super.add( k, new Tuple3<V1, V2, V3>(v1, v2, v3));
   }
}