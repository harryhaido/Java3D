package ass1;
/* Copyright material for students taking COMP-2800 to work on assignment/labs/projects. */

import java.awt.Font;


import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.Link;
import org.jogamp.vecmath.*;




public abstract class Assignment1ShapesHN {
	protected abstract Node create_Object();	           // use 'Node' for both Group and Shape3D
	public abstract Node position_Object();
}
class Base extends Assignment1ShapesHN	{
	public BranchGroup fourPrimitiveShape() {
		float a = 0f;
		float b = 0;
		float c = 0;
		Vector3f[] position = {new Vector3f(0.5f + b, -a, -0.5f + c), new Vector3f(-0.5f + b, -a, -0.5f + c),		//The location of each shape
				new Vector3f(0.5f + b, -a, 0.5f + c), new Vector3f(-0.5f + b, -a, 0.5f+ c), new Vector3f(b,-a,c), new Vector3f(b,-a,c)};
		
		BranchGroup objBG = new BranchGroup();
		Appearance app = CommonsHN.obj_Appearance(CommonsHN.Orange);		//appearance for the base
		
		SharedGroup shared3D = new SharedGroup( );
		shared3D.addChild(new Cylinder(0.5f, 0.2f, Primitive.GENERATE_NORMALS, 30, 30, app));		//create a cylinder
		shared3D.compile( );
		
		SharedGroup shared3D_box1 = new SharedGroup( );							//create the first box
		shared3D_box1.addChild(new Box(1.0f, 0.1f, 0.5f, app));
		shared3D_box1.compile( );

		SharedGroup shared3D_box2 = new SharedGroup( );							//create the second box
		shared3D_box2.addChild(new Box(0.5f, 0.1f, 1.0f, app));
		shared3D_box2.compile( );

		Transform3D trans3d = new Transform3D();
		TransformGroup trans = null;
		Link[] links = new Link[6];
		for (int i = 0; i < 6; i++) {
			if(i<4)	{
				links[i] = new Link(shared3D);					//shared group for the cylinder
			}
			if(i==4) {
				links[i] = new Link(shared3D_box1);
			}
			if(i==5)	{
				links[i] = new Link(shared3D_box2);
			}
			trans3d.setTranslation(position[i]);		//use translation to place cylinder in the right position
			trans = new TransformGroup(trans3d);
			objBG.addChild(trans);					
			trans.addChild(links[i]);
		}
		return objBG;
	}
	private TransformGroup objTG;                              // use 'objTG' to position an object
	public Base()	{
		Transform3D translation = new Transform3D();           
		translation.setTranslation(new Vector3f(0f, -1.1f, 0f));		//vector for translation

		Transform3D scaler = new Transform3D();
		scaler.setScale(new Vector3d(1f, 1f, 0.25f));			//vector for scaling

		Transform3D trfm = new Transform3D();           
		trfm.mul(translation); 							// apply translation first
		trfm.mul(scaler);                              // apply scaler next
		                                
		objTG = new TransformGroup(trfm);			//apply for objTG
		objTG.addChild(create_Object());		   
	}
	protected Node create_Object()	{
		return fourPrimitiveShape();
	}
	public Node position_Object() {
		return objTG;
	}
}

class XYZ extends Assignment1ShapesHN	{
	int si;
	public XYZ(int n)	{				//length of XYZ
		si = n;
	}
	protected Node create_Object()	{
		Point3f coor[] = new Point3f[4];                   // declare 4 points for XYZ
		LineArray lineArr = new LineArray(8, LineArray.COLOR_3 | LineArray.COORDINATES);
		coor[0] = new Point3f(0, 0, 0); 
		coor[1] = new Point3f(si, 0, 0); 				//Position 1
		coor[2] = new Point3f(0, si, 0); 				//Position 2
		coor[3] = new Point3f(0, 0, si); 				//Position 3
		
			lineArr.setCoordinate(0, coor[0]);         // define point pairs for each line
			lineArr.setColor(0, CommonsHN.Red);        // specify color for each pair of points
			lineArr.setCoordinate(1, coor[1]);         // define point pairs for each line
			lineArr.setColor(1, CommonsHN.Red);        // specify color for each pair of points
			lineArr.setCoordinate(2, coor[0]);         // define point pairs for each line
			lineArr.setColor(2, CommonsHN.Green);        // specify color for each pair of points
			lineArr.setCoordinate(3, coor[2]);         // define point pairs for each line
			lineArr.setColor(3, CommonsHN.Green);        // specify color for each pair of points
			lineArr.setCoordinate(4, coor[0]);         // define point pairs for each line
			lineArr.setColor(4, CommonsHN.Blue);        // specify color for each pair of points
			lineArr.setCoordinate(5, coor[3]);         // define point pairs for each line
			lineArr.setColor(5, CommonsHN.Blue);        // specify color for each pair of points
		return new Shape3D(lineArr);                        // create and return a Shape3D
	}
	public Node position_Object()	{
		return create_Object();
	}
}


class StringShape extends Assignment1ShapesHN {
	private TransformGroup objTG;                              // use 'objTG' to position an object
	private String str;
	public StringShape(String str_ltrs) {
		str = str_ltrs;		
		
		Transform3D translation = new Transform3D();           // 4x4 matrix for rotation
		translation.setTranslation(new Vector3f(0f, -1.15f, 0f));
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.15);                              // scaling 4x4 matrix 
		
		Transform3D rotator = new Transform3D();           // 4x4 matrix for rotation
		rotator.rotY(Math.PI);
		
		
		Transform3D trfm = new Transform3D();              // 4x4 matrix for composition
		
		trfm.mul(translation);
		trfm.mul(scaler);                              // apply scaler first
		trfm.mul(rotator);                                 // apply rotation next
		
		objTG = new TransformGroup(trfm);                  // set the combined transformation
		objTG.addChild(create_Object());		   // apply scaling to change the string's size
	}
	protected Node create_Object() {
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);  // font's name, style, size
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);		
		Appearance app = CommonsHN.obj_Appearance(CommonsHN.Magenta);
		Point3f pos = new Point3f(-str.length()/4f, 0f, 1.5f);// position for the string 
		Text3D text3D = new Text3D(font3D, str, pos);      // create a text3D object
		return new Shape3D(text3D, app);
	}
	public Node position_Object() {
		return objTG;
	}
}
