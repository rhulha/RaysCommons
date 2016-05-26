package net.raysforge.commons;

import java.util.HashMap;
import java.util.Hashtable;

public class TestRayString {
	
	
	public static void main(String args[])
    {
		System.err.println("start");

        RayString rs =
            new RayString(
                "RAYdasdsadercr545bx<RAY>x56g675g76hx</RAY>x56xxg68ugw45gu85rua4y5mgfjrtx"
                    + "xretj tuevmrotvertvjurtruevraRAYymivtxxmvtrmivtrmuvtrmreumv3893457878943v8xx989vn8945fm948f5m94xx8v5nq394f5zq34789RAY");

        /*      int o=0;
              while( (o = rs.indexi( "ray", o)) != -1)
              {
                o++;
              } */

        RayString rs2[] = rs.split("RAY");

        for (int z = 0; z < rs2.length; z++)
            System.err.println(":" + rs2.length + ":" + rs2[z]);

        /*      System.err.println( "" +  rs.join( "aa", rs2) );
        
              for( int x=0; x < 40000; x++)
              {
                 rs = rs.replace( "v","!!!");
                 rs = rs.replace( "!!!","*************");
              }
        
              System.err.println( "" +  (rs = rs.replace( "v","!!!") ));
              System.err.println( "" + rs.replace( "!!!","*************") );
        */

        RayString rs_1 = new RayString("asdffwert");
        RayString rs_2 = new RayString("asdffwert");
        if (!rs_1.equals(rs_2))
            System.out.println("ahh");
        if (rs_1.hashCode() != rs_2.hashCode())
            System.out.println("ahh 2");

        HashMap<RayString, String> vars = Generics.newHashMap();
        Hashtable<RayString, String> ht = new Hashtable<RayString, String>();

        System.err.println(RayString.hash(rs_1) + ":" + RayString.hash(rs_2));
        vars.put(rs_1, "" + 23423);
        if (vars.containsKey(rs_2))
            System.out.println("2q34");

        ht.put(rs_1, "" + 23423);
        //        ht.put(rs_2, "" + 23423);
        if (ht.containsKey(rs_2))
            System.out.println("2q34");
    }
}
